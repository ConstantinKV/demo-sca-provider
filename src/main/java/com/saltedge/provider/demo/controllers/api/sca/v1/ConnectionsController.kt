/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.controllers.api.sca.v1

import com.saltedge.provider.demo.config.ApplicationProperties
import com.saltedge.provider.demo.config.SCA_CONNECT_QUERY_PREFIX
import com.saltedge.provider.demo.controllers.api.sca.v1.model.*
import com.saltedge.provider.demo.errors.BadRequest
import com.saltedge.provider.demo.errors.NotFound
import com.saltedge.provider.demo.model.ScaConnectionEntity
import com.saltedge.provider.demo.model.ScaConnectionsRepository
import com.saltedge.provider.demo.tools.security.CryptoTools
import com.saltedge.provider.demo.tools.security.KeyTools
import com.saltedge.provider.demo.tools.toJson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.SecretKey

@RestController
@RequestMapping(ConnectionsController.BASE_PATH)
class ConnectionsController : BaseController() {
    companion object {
        const val BASE_PATH: String = "$API_BASE_PATH/connections"
    }

    @Autowired
    lateinit var applicationProperties: ApplicationProperties

    @Autowired
    lateinit var connectionsRepository: ScaConnectionsRepository

    @PostMapping
    fun create(@RequestBody request: CreateConnectionRequest): ResponseEntity<CreateConnectionResponse> {
        try {
            val dhPrivateKey = applicationProperties.dhPrivateKey
            val authDhPublicKey = KeyTools.convertPemToPublicKey(request.data.dhPublicKey, KeyTools.Algorithm.DIFFIE_HELLMAN)
                ?: throw BadRequest.WrongRequestFormat(errorMessage = "invalid dh public key")
            val sharedSecret: SecretKey = KeyTools.computeSecretKey(dhPrivateKey, authDhPublicKey)
            val authRsaPublicKeyPem = CryptoTools.decryptAes(request.data.encRsaPublicKey, sharedSecret) ?: ""
            KeyTools.convertPemToPublicKey(authRsaPublicKeyPem, KeyTools.Algorithm.RSA)
                ?: throw BadRequest.WrongRequestFormat(errorMessage = "invalid rsa public key")

            val userId = request.data.connectQuery?.replace(SCA_CONNECT_QUERY_PREFIX, "")
            val responseData = if (userId == null) {
                createErrorResponse(request.data.returnUrl)
            } else {
                val accessToken = UUID.randomUUID().toString()
                createScaConnectionEntity(request, authRsaPublicKeyPem, accessToken)
                val encryptedJson = CryptoTools.encryptAes(AccessTokenResponse(accessToken).toJson() ?: "", sharedSecret) ?: ""
                CreateConnectionResponseData(
                    authenticationUrl = "${request.data.returnUrl}?access_token=$encryptedJson",
                    userId = userId,
                    accessToken = accessToken,
                    rsaPublicKey = authRsaPublicKeyPem
                )
            }

            return ResponseEntity(CreateConnectionResponse(data = responseData), HttpStatus.OK)
        } catch (e: Exception) {
            println(e.message)
            e.printStackTrace()
            throw BadRequest.WrongRequestFormat(errorMessage = "Internal error")
        }
    }

    @PutMapping("/{connection_id}/revoke")
    fun revoke(@PathVariable("connection_id") connectionId: String): ResponseEntity<UpdateConnectionResponse> {
        connectionsRepository.findFirstByConnectionId(connectionId)?.let {
            it.revoked = true
            connectionsRepository.save(it)
        } ?: throw NotFound.ConnectionNotFound()
        return ResponseEntity(UpdateConnectionResponse(), HttpStatus.OK)
    }

    private fun createErrorResponse(returnUrl: String): CreateConnectionResponseData {
        val error = URLEncoder.encode("Invalid connect query", StandardCharsets.UTF_8.toString())
        return CreateConnectionResponseData(
            authenticationUrl = "$returnUrl?error_class=AUTHENTICATION_FAILED&error_message=$error"
        )
    }

    private fun createScaConnectionEntity(request: CreateConnectionRequest, authRsaPublicKeyPem: String, accessToken: String) {
        val entity = ScaConnectionEntity()
        entity.connectionId = request.data.connectionId
        entity.dhPublicKey = request.data.dhPublicKey
        entity.rsaPublicKey = authRsaPublicKeyPem
        entity.returnUrl = request.data.returnUrl
        entity.accessToken = accessToken
        connectionsRepository.save(entity)
    }
}
