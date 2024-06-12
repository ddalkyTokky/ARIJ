package com.arij.ajir.domain.issue.service

import com.arij.ajir.domain.issue.dto.IssueCreateRequest
import com.arij.ajir.domain.issue.dto.IssueResponse
import com.arij.ajir.domain.issue.dto.IssueUpdateRequest
import com.arij.ajir.domain.issue.model.Issue
import com.arij.ajir.domain.issue.model.Priority
import com.arij.ajir.domain.issue.repository.IssueRepository
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class IssueService(
    private val issueRepository: IssueRepository,
    // todo(private val memberRepository: MemberRepository,
    // todo(private val teamRepository: TeamRepository,
) {
    fun getAllIssues(
        topic: String,
        keyword: String,
        orderBy: String,
        ascend: Boolean,
    ): List<IssueResponse> {
        val pageable = if (ascend) Sort.by(orderBy).ascending() else Sort.by(orderBy).descending()
    }

    fun getIssueById(id: Long): IssueResponse {
        val issue = issueRepository.findIssueByIdAndDeleteStatusIsFalse(id)
            .orElseThrow() { IllegalStateException("Issue not found") }

        return issue.toResponse()
    }

    @Transactional
    fun createIssue(request: IssueCreateRequest): IssueResponse {
        // todo(member = memberRepository)
        // todo(team = teamRepository)
        val issue = Issue.createIssue(request, member, team)

        return issueRepository.save(issue).toResponse()
    }

    @Transactional
    fun updateIssue(issueId: Long, request: IssueUpdateRequest): IssueResponse {
        val (title, content, category) = request
        val issue = issueRepository.findIssueByIdAndDeleteStatusIsFalse(issueId)
            .orElseThrow() { IllegalStateException("Issue not found") }

        issue.title = title
        issue.content = content
        issue.category = category

        return issue.toResponse()
    }

    @Transactional
    fun updatePriority(issueId:Long, newPriority: Priority):IssueResponse {
        val issue = issueRepository.findIssueByIdAndDeleteStatusIsFalse(issueId)
            .orElseThrow() { IllegalStateException("Issue not found") }
        issue.priority = newPriority

        return issueRepository.save(issue).toResponse()
    }

    @Transactional
    fun deleteIssue(id: Long) {
        val issue = issueRepository.findIssueByIdAndDeleteStatusIsFalse(id)
            .orElseThrow() { IllegalStateException("Issue not found") }

        issue.delete()
        issueRepository.save(issue)
    }
}