package com.arij.ajir.domain.issue.repository

import com.arij.ajir.domain.issue.model.Issue

interface CustomIssueRepository {

    fun searchIssues(
        topic: String?,
        keyword: String?,
        orderBy: String,
        ascend: Boolean,
        teamId: Long
    ): List<Issue>

}