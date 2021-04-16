/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.callback

import com.fasterxml.jackson.databind.ObjectMapper
import com.saltedge.provider.demo.config.ApplicationProperties
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
    lateinit var applicationProperties: ApplicationProperties
    @Autowired
    @Qualifier("ScaServiceCallbackRestTemplateBean")
    lateinit var callbackRestTemplate: RestTemplate
    private var mapper: ObjectMapper = JsonTools.createDefaultMapper()

    @Bean
    @Qualifier("ScaServiceCallbackRestTemplateBean")
    fun createRestTemplate(): RestTemplate = RestTemplate()

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
        val url: String = applicationProperties.scaServiceUrl + "/api/sca/v1/actions"
        val signature = JwsTools.encode(requestData = request.data, expiresAt = requestExpiresAt, key = applicationProperties.scaServiceRsaPublicKey)
        val result = doCallbackRequest(url, signature, request)
        println("sendActionCreateCallback:result: " + result?.body?.toString())
    }

    private fun doCallbackRequest(url: String, signature: String, request: Any): ResponseEntity<Any>? {
        try {
            val headers = HttpHeaders()
            headers.contentType = MediaType.APPLICATION_JSON
            headers.set("Provider-Id", applicationProperties.scaProviderId)
            headers.set("x-jws-signature", signature)

            return callbackRestTemplate.exchange(url, HttpMethod.POST, HttpEntity(request, headers), Any::class.java)
        } catch (e: HttpClientErrorException) {
            e.printStackTrace()
        } catch (e: HttpServerErrorException) {
            e.printStackTrace()
        } catch (e: UnknownHttpStatusCodeException) {
            e.printStackTrace()
        }
        return null
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
