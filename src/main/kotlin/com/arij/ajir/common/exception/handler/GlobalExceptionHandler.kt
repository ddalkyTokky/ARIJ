package com.arij.ajir.common.exception.handler

import com.arij.ajir.common.exception.*
import com.arij.ajir.common.exception.dto.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateArgumentException::class)
    fun duplicateArgumentException(e: RuntimeException): ResponseEntity<ErrorResponse> {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse(409, e.message ?: "중복 애러"))
    }

    @ExceptionHandler(InvalidCredentialException::class)
    fun invalidCredentialException(e: RuntimeException): ResponseEntity<ErrorResponse> {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse(401, e.message ?: "인증 에러"))
    }

    @ExceptionHandler(ModelNotFoundException::class)
    fun modelNotFoundException(e: ModelNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse(404, e.message ?: "존재 하지 않음"))
    }

    @ExceptionHandler(ModelNotSavedException::class)
    fun modelNotSavedException(e: RuntimeException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse(400, e.message ?: "세이브 되지 않음"))
    }

    @ExceptionHandler(NotAuthorityException::class)
    fun notAuthorityException(e: NotAuthorityException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorResponse(403, e.message ?: "권한 없음"))
    }

    @ExceptionHandler(TokenException::class)
    fun tokenException(e: RuntimeException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse(404, e.message ?: "토큰 없음"))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun illegalArgumentException(e: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse(404, e.message ?: "잘못된 접근"))

    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun methodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse(404, "잘못된 요청 내용?"))
    }
}