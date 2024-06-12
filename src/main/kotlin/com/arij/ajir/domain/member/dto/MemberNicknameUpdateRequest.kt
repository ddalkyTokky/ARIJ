package com.arij.ajir.domain.member.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class MemberNicknameUpdateRequest (
    //TODO 토큰생성 전에 임시로 사용할 email.
    val email: String,

    @field:NotBlank(message = "The nickname cannot be blank.")
    @field:Size(min = 4, max = 10, message = "Nickname must be between 4 and 10")
    @field:Pattern(regexp = "^[a-zA-Z0-9]+\$")
    val nickname: String
)