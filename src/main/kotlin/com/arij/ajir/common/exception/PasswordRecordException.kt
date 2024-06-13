package com.arij.ajir.common.exception

data class PasswordRecordException (
    override val message: String? = "Password already exist or uesd in 3 times!!"
): RuntimeException()