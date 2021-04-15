/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.controllers.api.sca.v1

import com.saltedge.provider.demo.tools.security.JwsTools
import org.springframework.web.servlet.HandlerInterceptor
import java.io.IOException
import java.util.stream.Collectors
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class SignatureInterceptor : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        return JwsTools.isSignatureValid(
            jwsSignature = request.getHeader("x-jws-signature") ?: "",
            rawRequestBody = request.getRequestBody()  ?: ""
        )
    }

    private fun HttpServletRequest.getRequestBody(): String? {
        return try {
            this.reader.lines().collect(Collectors.joining(System.lineSeparator()))
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}