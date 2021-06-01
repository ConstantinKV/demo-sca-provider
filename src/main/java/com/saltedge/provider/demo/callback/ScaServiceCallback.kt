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
import org.springframework.web.client.RestTemplate
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
        val authorization = createAuthorizationData(action)
        val encAuthorizations = connections.map { createEncryptedEntity(data = authorization, connection = it) }
        val requestExpiresAt = Instant.now().plus(2, ChronoUnit.MINUTES)
        val request = CreateActionRequest(
            data = CreateActionRequestData(
                action_id = action.id.toString(),
                user_id = SCA_USER_ID,
                expires_at = action.expiresAt.toString(),
                authorizations = encAuthorizations
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

    private fun createAuthorizationData(action: ScaActionEntity): String {
        val description = when (action.descriptionType) {
            "html" -> DescriptionData(html = "<body><p><b>TPP</b> is requesting your authorization to access account information data from <b>Demo Bank</b></p></body>")
            "json" -> {
                DescriptionData(payment = DescriptionPaymentData(
                    payee = "TPP",
                    amount = "100.0",
                    account = "MD24 AG00 0225 1000 1310 4168",
                    payment_date = action.createdAtDescription,
                    reference = "X1",
                    fee = "No fee",
                    exchange_rate = "1.0"
                ))
            }
            else -> DescriptionData(text = "TPP is requesting your authorization to access account information data from Demo Bank")
        }
        description.extra = ExtraData(action_date = "Today", device = "Google Chrome", location = "Munich, Germany", ip = "127.0.0.0")
        val authorizationData = AuthorizationData(
            title = "Access account information",
            description = description,
            authorization_code = action.code,
            created_at = action.createdAtValue.toString(),
            expires_at = action.expiresAt.toString()
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
            connection_id = connection.connectionId,
            key = Base64.getEncoder().encodeToString(CryptoTools.encryptRsa(key, publicKey)),
            iv = Base64.getEncoder().encodeToString(CryptoTools.encryptRsa(iv, publicKey)),
            data = Base64.getEncoder().encodeToString(CryptoTools.encryptAes(data, key, iv))
        )
    }
}
