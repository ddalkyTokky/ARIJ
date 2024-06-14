package com.arij.ajir.domain.member.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class MemberNicknameUpdateRequest (

    @field:Pattern(regexp = "(?=.*[a-z])(?=.*\\d)[a-z\\d]+", message = "nickname pattern is wrong")
    @field:NotBlank(message = "The nickname cannot be blank.")
    @field:Size(min = 4, max = 10, message = "Nickname must be between 4 and 10")
    val nickname: String
)