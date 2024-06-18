package com.arij.ajir.domain.issue.dto

import com.arij.ajir.domain.issue.model.Priority
import com.arij.ajir.domain.issue.model.WorkingStatus
import jakarta.validation.constraints.NotBlank

data class IssueCreateRequest(
    @field: NotBlank
    val title: String,
    @field: NotBlank
    val content: String,
    val priority: Priority,
    val workingStatus: WorkingStatus
)
