/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.errors

import org.springframework.http.HttpStatus

interface HttpErrorParams {
    val errorStatus: HttpStatus
    val errorClass: String
    val errorMessage: String
}
