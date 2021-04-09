/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.controllers.api.sca.v1

import com.saltedge.provider.demo.config.ApplicationProperties
import com.saltedge.provider.demo.errors.BadRequest
import io.jsonwebtoken.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.HandlerInterceptor
import java.io.IOException
import java.util.*
import java.util.stream.Collectors
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class SignatureInterceptor : HandlerInterceptor {
    @Autowired
    var applicationProperties: ApplicationProperties? = null

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        return isSignatureValid(jwsSignature = request.getHeader("x-jws-signature"), rawRequestBody = request.getRequestBody())
    }

    @Throws(BadRequest::class)
    private fun isSignatureValid(jwsSignature: String?, rawRequestBody: String?): Boolean {
        if (jwsSignature == null) throw BadRequest.SignatureMissing()
        if (rawRequestBody == null || rawRequestBody.isEmpty()) throw BadRequest.WrongRequestFormat()
        try {
            val jwsParts = jwsSignature.split(".").toMutableList()
            jwsParts[1] = Base64.getUrlEncoder().withoutPadding().encodeToString(rawRequestBody.toByteArray(Charsets.UTF_8))
            val encodedJws = jwsParts.joinToString(".")
            val claims: Jws<Claims> = Jwts.parserBuilder()
                .setSigningKey(applicationProperties?.scaServicePublicRsaKey)
                .build()
                .parseClaimsJws(encodedJws)
            return true
        } catch (e: ExpiredJwtException) {
            throw BadRequest.SignatureExpired()
        } catch (e: JwtException) {
            throw BadRequest.InvalidSignature(e.message ?: "JwtException")
        } catch (e: Exception) {
            throw BadRequest.WrongRequestFormat(e.message ?: "WrongRequestFormat")
        }
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