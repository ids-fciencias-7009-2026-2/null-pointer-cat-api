package com.nullpointercats.sys.adopta.config

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import java.time.LocalDateTime

/**
 * Global exception handler for the application's controller layer.
 * Intercepts exceptions thrown by any endpoint, so the client
 * gets HTTP error responses.
 */
@ControllerAdvice
class ExceptionHandler {

    /**
     * Handles [NoSuchElementException] when a requested resource
     * is missing from the database.
     *
     * @return [HttpStatus.NOT_FOUND] status and error body.
     */
    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(ex: NoSuchElementException, request: WebRequest): ResponseEntity<Any> {
        val body = mapOf(
            "timestamp" to LocalDateTime.now(),
            "status" to HttpStatus.NOT_FOUND.value(),
            "error" to "Not Found",
            "message" to ex.message,
            "path" to request.getDescription(false).replace("uri=", "")
        )
        return ResponseEntity(body, HttpStatus.NOT_FOUND)
    }
}