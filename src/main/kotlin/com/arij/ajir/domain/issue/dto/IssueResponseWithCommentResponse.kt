package com.arij.ajir.domain.issue.dto

import com.arij.ajir.domain.comment.dto.CommentResponse
import com.arij.ajir.domain.issue.model.Priority
import com.fasterxml.jackson.annotation.JsonFormat
import java.sql.Timestamp
import java.time.LocalDateTime

data class IssueResponseWithCommentResponse (
    val id: Long,
    val title: String,
    val author: String,
    val teamName: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    val createdAt: LocalDateTime,
    val content: String,
    val priority: Priority,
    val deleted: Boolean,
    val comments: List<CommentResponse>
)