/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.callback

import com.fasterxml.jackson.databind.ObjectMapper
import com.saltedge.provider.demo.config.DemoApplicationProperties
import com.saltedge.provider.demo.config.SCA_USER_ID
import com.saltedge.provider.demo.model.ScaActionEntity
import com.saltedge.provider.demo.model.ScaConnectionEntity
import com.saltedge.provider.demo.tools.JsonTools
import com.saltedge.provider.demo.tools.security.CryptoTools
import com.saltedge.provider.demo.tools.security.JwsTools
import com.saltedge.provider.demo.tools.security.KeyTools
import com.saltedge.provider.demo.tools.toJson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.http.*
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.UnknownHttpStatusCodeException
import java.security.SecureRandom
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@Service
class ScaServiceCallback {

    @Autowired
    lateinit var demoApplicationProperties: DemoApplicationProperties
    @Autowired
    @Qualifier("ScaServiceCallbackRestTemplateBean")
    lateinit var scaCallbackRest: RestTemplate
    private var mapper: ObjectMapper = JsonTools.createDefaultMapper()

    @Bean
    @Qualifier("ScaServiceCallbackRestTemplateBean")
    fun createScaRest(): RestTemplate = RestTemplate()

    @Async
    fun sendSuccessAuthenticationCallback(scaConnectionId: String, accessToken: String, rsaPublicKey: String) {
        val requestExpiresAt = Instant.now().plus(2, ChronoUnit.MINUTES)
        val request = SuccessAuthenticationRequest(
            data = SuccessAuthenticationRequestData(
                user_id = SCA_USER_ID,
                access_token = accessToken,
                rsa_public_key = rsaPublicKey
            ),
            exp = requestExpiresAt.epochSecond.toInt()
        )
        val url: String = demoApplicationProperties.scaServiceUrl + "/api/sca/v1/connections/$scaConnectionId/success_authentication"
        val signature = JwsTools.encode(requestData = request.data, expiresAt = requestExpiresAt, key = demoApplicationProperties.rsaPrivateKey)
        val result = doCallbackRequest(HttpMethod.PUT, url, signature, request)
        println("sendSuccessAuthenticationCallback:statusCode: ${result?.statusCode}")
    }

    @Async
    fun sendFailAuthenticationCallback(scaConnectionId: String, failMessage: String) {
        val requestExpiresAt = Instant.now().plus(2, ChronoUnit.MINUTES)
        val request = FailAuthenticationRequest(
            data = FailAuthenticationRequestData(fail_message = failMessage),
            exp = requestExpiresAt.epochSecond.toInt()
        )
        val url: String = demoApplicationProperties.scaServiceUrl + "/api/sca/v1/connections/$scaConnectionId/fail_authentication"
        val signature = JwsTools.encode(requestData = request.data, expiresAt = requestExpiresAt, key = demoApplicationProperties.rsaPrivateKey)
        val result = doCallbackRequest(HttpMethod.PUT, url, signature, request)
        println("sendFailAuthenticationCallback:statusCode: ${result?.statusCode}")
    }

    @Async
    fun sendActionCreateCallback(action: ScaActionEntity, connections: List<ScaConnectionEntity>) {
        val authorizations = connections.map {
            createEncryptedEntity(data = createAuthorizationData(action.code), connection = it)
        }
        val requestExpiresAt = Instant.now().plus(2, ChronoUnit.MINUTES)
        val request = CreateActionRequest(
            data = CreateActionRequestData(
                actionId = action.id.toString(),
                userId = SCA_USER_ID,
                expiresAt = authorizationExpiresAt,
                authorizations = authorizations
            ),
            exp = requestExpiresAt.epochSecond.toInt()
        )
        val url: String = demoApplicationProperties.scaServiceUrl + "/api/sca/v1/actions"
        val signature = JwsTools.encode(requestData = request.data, expiresAt = requestExpiresAt, key = demoApplicationProperties.rsaPrivateKey)
        val result = doCallbackRequest(HttpMethod.POST, url, signature, request)
        println("sendActionCreateCallback:result: " + result?.body?.toString())
    }

    private fun doCallbackRequest(method: HttpMethod, url: String, signature: String, request: Any): ResponseEntity<Any>? {
        return try {
            val headers = HttpHeaders()
            headers.contentType = MediaType.APPLICATION_JSON
            headers.set("Provider-Id", demoApplicationProperties.scaProviderId)
            headers.set("x-jws-signature", signature)

            scaCallbackRest.exchange(url, method, HttpEntity(request, headers), Any::class.java)
        } catch (e: Exception) {
            println("doCallbackRequest exception: $url" )
            e.printStackTrace()
            null
        }
    }

    private fun createAuthorizationData(authorizationCode: String): String {
        val authorizationData = AuthorizationData(
            title = "Create payment",
            description = DescriptionData(text = "TPP is requesting your authorization to access account information data from Demo Bank"),
            extra = ExtraData(actionDate = "Today", device = "Google Chrome", location = "Munich, Germany", ip = "127.0.0.0"),
            authorizationCode = authorizationCode,
            createdAt = Instant.now().toString(),
            expiresAt = authorizationExpiresAt
        )
        return authorizationData.toJson() ?: ""
    }

    /**
     * AES-256-CBC
     */
    private fun createEncryptedEntity(data: String, connection: ScaConnectionEntity): CreateActionAuthorization {
        val publicKey = KeyTools.convertPemToPublicKey(connection.rsaPublicKey, KeyTools.Algorithm.RSA)

        val key = ByteArray(32)
        SecureRandom().nextBytes(key)
        val iv = ByteArray(16)
        SecureRandom().nextBytes(iv)

        return CreateActionAuthorization(
            connectionId = connection.connectionId,
            key = Base64.getEncoder().encodeToString(CryptoTools.encryptRsa(key, publicKey)),
            iv = Base64.getEncoder().encodeToString(CryptoTools.encryptRsa(iv, publicKey)),
            data = Base64.getEncoder().encodeToString(CryptoTools.encryptAes(data, key, iv))
        )
    }

    private val authorizationExpiresAt: String
        get() = Instant.now().plus(10, ChronoUnit.MINUTES).toString()
}
