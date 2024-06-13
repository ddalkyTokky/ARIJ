package com.arij.ajir.common.exception.dto

data class ErrorResponse(
    val errorCode: Int,
    val errorMessage: String,
)
