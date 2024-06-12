package com.arij.ajir.domain.member.dto

import jakarta.validation.constraints.*

data class MemberRequest (
    @field:Email(message = "The email is not valid.")
    @field:NotBlank(message = "The email is not valid.")
    val email: String,

    @field:NotBlank(message = "The nickname cannot be blank.")
    @field:Size(min = 4, max = 10, message = "Nickname must be between 4 and 10")
    val nickname: String,

    @field:Size(min = 8, max = 15, message = "Password must be between 8 and 15")
    @field:NotBlank(message = "The password cannot be blank.")
    val password: String,

    @field:NotBlank(message = "The password cannot be blank.")
    val authNumber: String
)
