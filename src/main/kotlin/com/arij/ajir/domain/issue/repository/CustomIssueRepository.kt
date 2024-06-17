package com.arij.ajir.domain.issue.repository

import com.arij.ajir.domain.issue.model.Issue
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustomIssueRepository {

    fun searchIssues(
        topic: String?,
        keyword: String?,
        orderBy: String,
        ascend: Boolean,
        teamId: Long
    ): List<Issue>

    // TODO: 지워도 됨
    fun findIssue(pageable: Pageable): Page<Issue>
}