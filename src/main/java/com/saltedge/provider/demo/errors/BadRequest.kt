/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.errors

import org.springframework.http.HttpStatus

/**
 * Set of BadRequest (400) errors
 */
abstract class BadRequest(errorMessage: String) : RuntimeException(), HttpErrorParams {
    private var _errorMessage: String = errorMessage

    override val errorStatus: HttpStatus
        get() = HttpStatus.BAD_REQUEST

    override val errorClass: String
        get() = javaClass.simpleName

    override val errorMessage: String
        get() = _errorMessage

    /* BadRequest successors  */
    class WrongRequestFormat(errorMessage: String = "Wrong Request Format.") : BadRequest(errorMessage)
    class SignatureMissing : BadRequest("Signature is missing.")
    class SignatureExpired : BadRequest("Expired Signature.")
    class InvalidSignature(errorMessage: String = "Invalid Signature.") : BadRequest(errorMessage)
    class ActionExpired : BadRequest("Action is expired and cannot be done.")
}