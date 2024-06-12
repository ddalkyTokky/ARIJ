package com.arij.ajir.domain.member.dto

data class MemberPasswordUpdateRequest (
    val oldPw: String,
    val newPw: String
)