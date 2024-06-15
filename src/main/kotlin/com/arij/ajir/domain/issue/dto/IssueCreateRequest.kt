package com.arij.ajir.domain.issue.dto

import com.arij.ajir.domain.issue.model.Priority
import com.arij.ajir.domain.issue.model.WorkingStatus

data class IssueCreateRequest(
    val title: String,
    val content: String,
    val priority: Priority,
    val category: String,
    val workingStatus: WorkingStatus
)
