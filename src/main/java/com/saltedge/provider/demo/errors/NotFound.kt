/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.errors

import org.springframework.http.HttpStatus

/**
 * Set of NotFound (404) errors
 */
abstract class NotFound(errorMessage: String) : RuntimeException(), HttpErrorParams {
    private var _errorMessage: String = errorMessage

    override val errorStatus: HttpStatus
        get() = HttpStatus.NOT_FOUND

    override val errorClass: String
        get() = javaClass.simpleName

    override val errorMessage: String
        get() = _errorMessage

    /* NotFound successors  */
    class UserNotFound : NotFound("User Not Found.")
    class ConnectionNotFound : NotFound("Connection Not Found.")
    class ActionNotFound : NotFound("Action Not Found.")
    class ConsentNotFound : NotFound("Consent Not Found.")
}
