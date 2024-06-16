package com.arij.ajir.common.exception

data class ModelNotFoundException(val modelName: String, val input: String): RuntimeException(
    " $modelName 의 $input 이/가 존재 하지 않습 니다 "
)