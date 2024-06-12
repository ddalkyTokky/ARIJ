package com.arij.ajir.common.exception

data class ModelNotFoundException(val modelName: String, val input: String): RuntimeException(
    "Model $modelName not found with given input: $input"
)