/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.controllers.api.sca.v1

import org.slf4j.LoggerFactory
import org.springframework.web.servlet.HandlerInterceptor
import java.io.IOException
import java.security.PublicKey
import java.util.stream.Collectors
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class SignatureInterceptor(private val scaServiceRsaPublicKey: PublicKey) : HandlerInterceptor {

    companion object {
        private val log = LoggerFactory.getLogger(SignatureInterceptor::class.java)
    }

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        return true
//        val requestContent = ContentCachingRequestWrapper(request)
//        return JwsTools.isSignatureValid(
//            jwsSignature = requestContent.getHeader("x-jws-signature") ?: "",
//            rawRequestBody = requestContent.getRequestBody()  ?: "",
//            key = scaServiceRsaPublicKey
//        )
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