package com.arij.ajir.domain.issue.dto

import jakarta.validation.constraints.NotBlank

data class IssueUpdateRequest(
    @field: NotBlank
    val title: String,
    @field: NotBlank
    val content: String
)
