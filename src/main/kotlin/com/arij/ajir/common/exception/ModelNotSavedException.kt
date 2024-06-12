package com.arij.ajir.common.exception

data class ModelNotSavedException (val modelName: String): RuntimeException(
    "Model $modelName doesn't have id. Store it in to DBMS First!!"
)