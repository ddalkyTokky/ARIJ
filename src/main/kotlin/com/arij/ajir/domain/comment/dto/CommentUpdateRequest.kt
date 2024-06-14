package com.arij.ajir.domain.comment.dto

import jakarta.validation.constraints.NotBlank

data class CommentUpdateRequest(
    @field: NotBlank()
    val content: String
)
