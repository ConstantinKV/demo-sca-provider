/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.errors

import com.fasterxml.jackson.annotation.JsonProperty

class ScaErrorResponse() {
    @JsonProperty("error_class")
    var errorClass: String? = null

    @JsonProperty("error_message")
    var errorMessage: String? = null

    constructor(errorClass: String?, errorMessage: String?) : this() {
        this.errorClass = errorClass
        this.errorMessage = errorMessage
    }

    constructor(ex: Exception) : this() {
        errorClass = ex.javaClass.simpleName
        errorMessage = ex.localizedMessage
        if (ex is HttpErrorParams) {
            errorClass = (ex as HttpErrorParams).errorClass
            errorMessage = (ex as HttpErrorParams).errorMessage
        }
    }

    constructor(params: HttpErrorParams) : this() {
        errorClass = params.errorClass
        errorMessage = params.errorMessage
    }
}