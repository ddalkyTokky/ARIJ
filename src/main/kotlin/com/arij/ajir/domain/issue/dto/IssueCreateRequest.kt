package com.arij.ajir.domain.issue.dto

import com.arij.ajir.domain.issue.model.Priority

data class IssueCreateRequest(
    val title: String,
    val content: String,
    val priority: Priority,
    val category: String,
)
