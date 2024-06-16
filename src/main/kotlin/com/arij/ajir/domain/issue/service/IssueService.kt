package com.arij.ajir.domain.issue.service

import com.arij.ajir.common.exception.ModelNotFoundException
import com.arij.ajir.domain.comment.repository.CommentRepository
import com.arij.ajir.domain.issue.dto.*
import com.arij.ajir.domain.issue.model.Issue
import com.arij.ajir.domain.issue.repository.IssueRepository
import com.arij.ajir.domain.issue.repository.QueryDslIssueRepository
import com.arij.ajir.domain.member.model.Member
import com.arij.ajir.domain.member.model.Role
import com.arij.ajir.domain.member.repository.MemberRepository
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class IssueService(
    private val issueRepository: IssueRepository,
    private val memberRepository: MemberRepository,
    private val queryDslIssueRepository: QueryDslIssueRepository
) {
    fun getAllIssues(
        topic: String,
        keyword: String,
        orderBy: String,
        ascend: Boolean,
    ): List<IssueResponse>? {
        val result = queryDslIssueRepository.searchTopicAndKeyword(topic,keyword)
        val pageable = if (ascend) result.sortedBy { orderBy } else result.sortedByDescending { orderBy }
        return pageable.map { it.toResponse() }
    }

    fun getIssueById(issueId: Long, email: String): IssueResponseWithCommentResponse {

        val issue = checkAuthority(issueId, email)

        return issue.toResponseWithCommentResponse()
    }

    fun createIssue(request: IssueCreateRequest, email: String): IssueIdResponse {
        val member: Member = memberRepository.findByEmail(email) ?: throw ModelNotFoundException("Member", email)

        if (member.role.name != Role.ADMIN.name) {
            if (member.team!!.name == "DUMMY") {
                throw IllegalStateException("Dummy team Can't CRUD")
            }
        }

        val issue = Issue(
            title = request.title,
            content = request.content,
            priority = request.priority,
            workingStatus = request.workingStatus,
            member = member,
            team = member.team!!
        )

        return issueRepository.save(issue).toIdResponse()
    }

    fun updateIssue(issueId: Long, request: IssueUpdateRequest, email: String) {

        val issue = checkAuthority(issueId, email)

        issue.updateTitleAndContent(request)

        issueRepository.save(issue)
    }

    fun updatePriority(issueId: Long, request: PriorityUpdateRequest, email: String) {

        val issue = checkAuthority(issueId, email)

        issue.updatePriority(request.priority)
    }

    fun updateWorkingStatus(issueId: Long, request: WorkingStatusUpdateRequest, email: String) {

        val issue = checkAuthority(issueId, email)

        issue.updateWorkingStatus(request.workingStatus)
    }

    fun deleteIssue(issueId: Long, email: String) {

        val issue = checkAuthority(issueId, email)

        issue.delete()
        issueRepository.save(issue)
    }

    private fun checkAuthority(issueId: Long, email: String): Issue {

        val member: Member = memberRepository.findByEmail(email) ?: throw ModelNotFoundException("Member", email)
        val issue = issueRepository.findIssueByIdAndDeletedIsFalse(issueId)
            .orElseThrow() { IllegalStateException("Issue not found") }

        if (member.role.name != Role.ADMIN.name) {
            if (member.team!!.name == "DUMMY") {
                throw IllegalStateException("Dummy team Can't CRUD")
            }
            if (issue.team != member.team) {
                throw IllegalStateException("team not same")
            }
        }

        return issue
    }
}