package com.arij.ajir.domain.member.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class LoginRequest (
    @field:Email(message = "The email is not valid.")
    @field:NotBlank(message = "The email is not valid.")
    val email: String,

    @field:Pattern(regexp = "(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?])[A-Za-z\\d!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]+", message = "password pattern is wrong")
    @field:Size(min = 8, max = 15, message = "Password must be between 8 and 15")
    @field:NotBlank(message = "The password cannot be blank.")
    val password: String
)