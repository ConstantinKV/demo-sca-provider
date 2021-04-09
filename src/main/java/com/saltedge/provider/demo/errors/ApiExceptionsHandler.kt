/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.errors

import org.hibernate.exception.ConstraintViolationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ApiExceptionsHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(BadRequest::class, NotFound::class)
    fun handleCustomException(ex: Exception, request: WebRequest?): ResponseEntity<ScaErrorResponse> {
        val errorStatus: HttpStatus =
            if (ex is HttpErrorParams) (ex as HttpErrorParams).errorStatus else HttpStatus.BAD_REQUEST
        val error = ScaErrorResponse(ex)
        log.error(error.toString())
        ex.printStackTrace()
        return ResponseEntity.status(errorStatus).body<ScaErrorResponse>(error)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleConstraintViolationException(e: ConstraintViolationException): ResponseEntity<ScaErrorResponse> {
        val error = ScaErrorResponse(errorClass = "WrongRequestFormat", errorMessage = e.message)
        log.error(e.toString())
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body<ScaErrorResponse>(error)
    }

    companion object {
        private val log = LoggerFactory.getLogger(ApiExceptionsHandler::class.java)
    }
}