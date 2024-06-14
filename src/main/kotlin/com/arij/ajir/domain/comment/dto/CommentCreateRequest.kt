package com.arij.ajir.domain.comment.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull


data class CommentCreateRequest(
    @field: NotBlank()
    val content: String
)
