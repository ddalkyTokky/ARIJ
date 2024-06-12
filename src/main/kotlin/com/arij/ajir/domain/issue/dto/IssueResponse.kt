package com.arij.ajir.domain.issue.dto

import com.arij.ajir.domain.issue.model.Priority
import java.sql.Timestamp

data class IssueResponse(
    val id: Long,
    val title: String,
    val author: String,
    val teamName: String,
    val createdAt: Timestamp,
    val content: String,
    val priority: Priority,
    val category: String,
    val deleteStatus: Boolean
)