package com.arij.ajir.common.exception

import org.springframework.security.core.AuthenticationException

class TokenException(
    val messege: String
): AuthenticationException(messege)