package com.kyle.auth.api

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, Any?>> =
        ResponseEntity.badRequest().body(mapOf("error" to ex.message))

    @ExceptionHandler(IllegalStateException::class, NoSuchElementException::class)
    fun handleState(ex: RuntimeException): ResponseEntity<Map<String, Any?>> =
        ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(mapOf("error" to ex.message))

    @ExceptionHandler(Exception::class)
    fun handleGeneric(ex: Exception): ResponseEntity<Map<String, Any?>> =
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapOf("error" to ex.message))
}