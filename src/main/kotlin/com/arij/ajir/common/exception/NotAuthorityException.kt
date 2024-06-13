package com.arij.ajir.common.exception

data class NotAuthorityException(
    val msg: String,
    val role: String
): RuntimeException(
    "$msg , 현재 사용자 권한 : $role"
)