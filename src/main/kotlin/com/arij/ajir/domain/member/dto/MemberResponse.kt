package com.arij.ajir.domain.member.dto

data class MemberResponse (
    val memberId: Long,
    val teamName: String,
    val email: String,
    val nickname: String,
    val role: String,
)