/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.callback

import com.fasterxml.jackson.databind.ObjectMapper
import com.saltedge.provider.demo.config.DemoApplicationProperties
import com.saltedge.provider.demo.config.SCA_USER_ID
import com.saltedge.provider.demo.model.ScaActionEntity
import com.saltedge.provider.demo.model.ScaConnectionEntity
import com.saltedge.provider.demo.model.ScaConsentEntity
import com.saltedge.provider.demo.tools.*
import com.saltedge.provider.demo.tools.security.JwsTools
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.http.*
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class ScaServiceCallback {

    @Autowired
    lateinit var demoApplicationProperties: DemoApplicationProperties
    @Autowired
//    @Qualifier("ScaServiceCallbackRestTemplateBean")
    lateinit var scaCallbackRest: RestTemplate

//    private var mapper: ObjectMapper = JsonTools.createDefaultMapper()

//    @Bean
//    @Qualifier("ScaServiceCallbackRestTemplateBean")
//    fun createScaRest(): RestTemplate {
//        val result = RestTemplate()
//        result.messageConverters.add(0, mappingJacksonHttpMessageConverter());
//        return result
//    }

//    @Bean
//    fun mappingJacksonHttpMessageConverter(): MappingJackson2HttpMessageConverter {
//        val messageConverter = MappingJackson2HttpMessageConverter()
//        messageConverter.setPrettyPrint(false)
//        messageConverter.objectMapper = mapper
//        return messageConverter
//    }

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
        println("sendSuccessAuthenticationCallback: [${Instant.now()}] scaConnectionId:$scaConnectionId result:${result?.statusCode}")
    }

    @Async
    fun sendFailAuthenticationCallback(scaConnectionId: String, failMessage: String) {
        val requestExpiresAt = Instant.now().plus(2, ChronoUnit.MINUTES)
        val request = FailAuthenticationRequest(
            data = FailAuthenticationRequestData(fail_message = failMessage),
            exp = requestExpiresAt.epochSecond.toInt()
        )
        val url = demoApplicationProperties.scaServiceUrl + "/api/sca/v1/connections/$scaConnectionId/fail_authentication"
        val signature = JwsTools.encode(requestData = request.data, expiresAt = requestExpiresAt, key = demoApplicationProperties.rsaPrivateKey)
        val result = doCallbackRequest(HttpMethod.PUT, url, signature, request)
        println("sendFailAuthenticationCallback: [${Instant.now()}] scaConnectionId:$scaConnectionId result:${result?.statusCode}")
    }

    @Async
    fun sendRevokeConnectionCallback(scaConnectionId: String) {
        val requestExpiresAt = Instant.now().plus(2, ChronoUnit.MINUTES)
        val request = RevokeConnectionRequest(exp = requestExpiresAt.epochSecond.toInt())
        val url = demoApplicationProperties.scaServiceUrl + "/api/sca/v1/connections/$scaConnectionId/revoke"
        val signature = JwsTools.encode(requestData = request.data, expiresAt = requestExpiresAt, key = demoApplicationProperties.rsaPrivateKey)
        val result = doCallbackRequest(HttpMethod.PUT, url, signature, request)
        println("sendRevokeConnectionCallback: [${Instant.now()}] scaConnectionId:$scaConnectionId result:${result?.statusCode}")
    }

    @Async
    fun sendActionCreateCallback(action: ScaActionEntity, connections: List<ScaConnectionEntity>) {
        val authorization = createAuthorizationData(action)
        val encAuthorizations = connections.map { createEncryptedAction(data = authorization, connection = it) }
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
        println("sendActionCreateCallback: [${Instant.now()}] action_id:${action.id} result:${result?.statusCode}")
    }

    @Async
    fun sendConsentCreateCallback(consent: ScaConsentEntity, connections: List<ScaConnectionEntity>) {
        val requestExpiresAt = Instant.now().plus(2, ChronoUnit.MINUTES)

        val consentData = createConsentData(consent)
        val encConsents = connections.map { createEncryptedEncryptedConsent(data = consentData, expiresAt = consent.expiresAt ?: requestExpiresAt, connection = it) }

        val request = CreateConsentRequest(
            data = CreateConsentRequestData(
                consent_id = consent.id.toString(),
                user_id = consent.userId,
                consents = encConsents
            ),
            exp = requestExpiresAt.epochSecond.toInt()
        )
        val url: String = demoApplicationProperties.scaServiceUrl + "/api/sca/v1/consents"
        val signature = JwsTools.encode(requestData = request.data, expiresAt = requestExpiresAt, key = demoApplicationProperties.rsaPrivateKey)
        val result = doCallbackRequest(HttpMethod.POST, url, signature, request)
        println("sendConsentCreateCallback: [${Instant.now()}] scaConsentId:${consent.id} result:${result?.statusCode}")
    }

    @Async
    fun sendRevokeConsentCallback(scaConsentId: String) {
        val requestExpiresAt = Instant.now().plus(2, ChronoUnit.MINUTES)
        val request = RevokeConnectionRequest(exp = requestExpiresAt.epochSecond.toInt())
        val url = demoApplicationProperties.scaServiceUrl + "/api/sca/v1/consents/$scaConsentId/revoke"
        val signature = JwsTools.encode(requestData = request.data, expiresAt = requestExpiresAt, key = demoApplicationProperties.rsaPrivateKey)
        val result = doCallbackRequest(HttpMethod.PUT, url, signature, request)
        println("sendRevokeConsentCallback: [${Instant.now()}] scaConsentId:$scaConsentId result:${result?.statusCode}")
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


}
