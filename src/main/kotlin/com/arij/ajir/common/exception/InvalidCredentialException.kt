package com.arij.ajir.common.exception

data class InvalidCredentialException(
 override val message: String? = "The Credential is invalid"
): RuntimeException()