package com.arij.ajir.domain.team.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class TeamRequest(

    @field:NotBlank(message = "The TeamName cannot be blank.")
    @field:Size(min = 1, max = 20, message = "TeamName must be between 1 and 20")
    val name: String,
)