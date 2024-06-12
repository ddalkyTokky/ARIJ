package com.arij.ajir.domain.member.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class MemberPasswordUpdateRequest (
    //TODO 토큰생성 전에 임시로 사용할 email.
    val email: String,

    val oldPw: String,

    @field:Size(min = 8, max = 15, message = "Password must be between 8 and 15")
    @field:NotBlank(message = "The password cannot be blank.")
    @field:Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]]+$")
    val newPw: String
)