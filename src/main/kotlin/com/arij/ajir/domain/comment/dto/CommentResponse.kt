package com.arij.ajir.domain.comment.dto

data class CommentResponse(
    val commentId: Long,
    val author: String,
    val content: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    val createdAt: LocalDateTime
)
