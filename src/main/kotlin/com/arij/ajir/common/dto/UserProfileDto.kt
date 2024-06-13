package com.arij.ajir.common.dto

data class UserProfileDto(
    val email: String,
    val role : String
){
    companion object {
        fun from(email: String, role : String) : UserProfileDto {

            return UserProfileDto(
                email = email,
                role = role
            )
        }
    }
}