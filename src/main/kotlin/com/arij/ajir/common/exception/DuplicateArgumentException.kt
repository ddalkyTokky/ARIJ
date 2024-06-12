package com.arij.ajir.common.exception

data class DuplicateArgumentException (val modelName: String, val input: String): RuntimeException(
    "Already Existed $modelName with input $input"
)