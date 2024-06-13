package com.arij.ajir.common.dto

import com.arij.ajir.infra.security.UserPrincipal

data class UserProfileDto(
    val email: String,
    val role : String
){
    companion object {
        fun from(userPrincipal: UserPrincipal) : UserProfileDto {
        val role = userPrincipal.authorities.first().toString().substringAfter("ROLE_")

            return UserProfileDto(
                email = userPrincipal.email,
                role = role
            )
        }
    }
}