package com.arij.ajir.domain.issue.service

import com.arij.ajir.common.exception.ModelNotFoundException
import com.arij.ajir.domain.issue.dto.*
import com.arij.ajir.domain.issue.model.Issue
import com.arij.ajir.domain.issue.model.Priority
import com.arij.ajir.domain.issue.repository.IssueRepository
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

    fun getIssueById(id: Long, email: String): IssueResponseWithCommentResponse {
        val member = memberRepository.findByEmail(email) ?: throw ModelNotFoundException("Member", email)
        val issue = issueRepository.findIssueByIdAndDeletedIsFalse(id)
            .orElseThrow() { IllegalStateException("Issue not found") }

        if (member.role.name != Role.ADMIN.name) {
            if (member.team!!.name == "DUMMY") {
                throw IllegalStateException("Dummy team Can't CRUD")
            }
            if (issue.team != member.team) {
                throw IllegalStateException("team not same")
            }
        }

        return issue.toResponseWithCommentResponse()
    }

    fun createIssue(request: IssueCreateRequest, email: String): IssueIdResponse {
        val member: Member = memberRepository.findByEmail(email) ?: throw ModelNotFoundException("Member", email)
        val issue = Issue.createIssue(request, member, member.team!!)

        if (member.role.name != Role.ADMIN.name) {
            if (member.team!!.name == "DUMMY") {
                throw IllegalStateException("Dummy team Can't CRUD")
            }
        }

        return issueRepository.save(issue).toIdResponse()
    }

    fun updateIssue(issueId: Long, request: IssueUpdateRequest, email: String) {
        val member: Member = memberRepository.findByEmail(email) ?: throw ModelNotFoundException("Member", email)
        val issue = issueRepository.findIssueByIdAndDeletedIsFalse(issueId)
            .orElseThrow() { IllegalStateException("Issue not found") }

        if (member.role.name != Role.ADMIN.name) {
            if (member.team!!.name == "DUMMY") {
                throw IllegalStateException("Dummy team Can't CRUD")
            }
            if (issue.member != member && issue.team != member.team) {
                throw IllegalStateException("member and team not same")
            }
        }
        issue.update(request)
    }

    fun updatePriority(issueId: Long, newPriority: Priority, email: String) {
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

        issue.priority = newPriority
    }

    fun deleteIssue(id: Long, email: String) {
        val member: Member = memberRepository.findByEmail(email) ?: throw ModelNotFoundException("Member", email)
        val issue = issueRepository.findIssueByIdAndDeletedIsFalse(id)
            .orElseThrow() { IllegalStateException("Issue not found") }

        if (member.role.name != Role.ADMIN.name) {
            if (member.team!!.name == "DUMMY") {
                throw IllegalStateException("Dummy team Can't CRUD")
            }
            if (issue.team != member.team) {
                throw IllegalStateException("team not same")
            }
        }

        issue.delete()
        issueRepository.save(issue)
    }
}