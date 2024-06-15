package com.arij.ajir.domain.issue.dto

import com.arij.ajir.domain.comment.dto.CommentResponse
import com.arij.ajir.domain.issue.model.Priority
import com.arij.ajir.domain.issue.model.WorkingStatus
import java.sql.Timestamp

data class IssueResponseWithCommentResponse (
    val id: Long,
    val title: String,
    val author: String,
    val teamName: String,
    val createdAt: Timestamp,
    val content: String,
    val priority: Priority,
    val workingStatus: WorkingStatus,
    val deleted: Boolean,
    val comments: List<CommentResponse>
)