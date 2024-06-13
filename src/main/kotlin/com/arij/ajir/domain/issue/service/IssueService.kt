package com.arij.ajir.domain.issue.service

import com.arij.ajir.domain.comment.model.toResponse
import com.arij.ajir.common.exception.ModelNotFoundException
import com.arij.ajir.domain.comment.repository.CommentRepository
import com.arij.ajir.domain.issue.dto.*
import com.arij.ajir.domain.issue.model.Issue
import com.arij.ajir.domain.issue.model.Priority
import com.arij.ajir.domain.issue.repository.IssueRepository
import com.arij.ajir.domain.member.model.Member
import com.arij.ajir.domain.member.repository.MemberRepository
import com.arij.ajir.domain.team.repository.TeamRepository
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class IssueService(
    private val issueRepository: IssueRepository,
    private val memberRepository: MemberRepository,
    private val teamRepository: TeamRepository,
    private val commentRepository: CommentRepository,
) {
    fun getAllIssues(
        topic: String,
        keyword: String,
        orderBy: String,
        ascend: Boolean,
    ): List<IssueResponse>? {
        val pageable = if (ascend) Sort.by(orderBy).ascending() else Sort.by(orderBy).descending()
        return null
    }

    fun getIssueById(id: Long): IssueResponseWithCommentResponse {
        val issue = issueRepository.findIssueByIdAndDeletedIsFalse(id)
            .orElseThrow() { IllegalStateException("Issue not found") }
        val comment = commentRepository.findAllByIssueId(id).map { it.toResponse() }

        return issue.toResponseWithCommentResponse(comment)
    }

    @Transactional
    fun createIssue(request: IssueCreateRequest, email: String): IssueIdResponse {
        val member: Member = memberRepository.findByEmail(email) ?: throw ModelNotFoundException("Member", email)

        val issue = Issue.createIssue(request, member, member.team!!)

        return issueRepository.save(issue).toIdResponse()
    }

    @Transactional
    fun updateIssue(issueId: Long, request: IssueUpdateRequest, email: String) {
        val (title, content, category) = request
        val member: Member = memberRepository.findByEmail(email) ?: throw ModelNotFoundException("Member", email)
        val issue = issueRepository.findIssueByIdAndDeletedIsFalse(issueId)
            .orElseThrow() { IllegalStateException("Issue not found") }

        if (issue.member != member && issue.team != member.team) {
            throw IllegalStateException("member and team not same")
        }

        issue.title = title
        issue.content = content
        issue.category = category
    }

    @Transactional
    fun updatePriority(issueId: Long, newPriority: Priority, email: String) {
        val member: Member = memberRepository.findByEmail(email) ?: throw ModelNotFoundException("Member", email)
        val issue = issueRepository.findIssueByIdAndDeletedIsFalse(issueId)
            .orElseThrow() { IllegalStateException("Issue not found") }

        if (issue.team != member.team) {
            throw IllegalStateException("team not same")
        }

        issue.priority = newPriority
    }

    @Transactional
    fun deleteIssue(id: Long, email: String) {
        val member: Member = memberRepository.findByEmail(email) ?: throw ModelNotFoundException("Member", email)
        val issue = issueRepository.findIssueByIdAndDeletedIsFalse(id)
            .orElseThrow() { IllegalStateException("Issue not found") }

        if (issue.team != member.team) {
            throw IllegalStateException("team not same")
        }

        issue.delete()
        issueRepository.save(issue)
    }
}