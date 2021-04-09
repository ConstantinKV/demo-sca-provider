/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.controllers.api.sca.v1

import com.saltedge.provider.demo.config.ApplicationProperties
import com.saltedge.provider.demo.controllers.api.sca.v1.model.AccessTokenResponse
import com.saltedge.provider.demo.controllers.api.sca.v1.model.CreateConnectionRequest
import com.saltedge.provider.demo.controllers.api.sca.v1.model.CreateConnectionResponse
import com.saltedge.provider.demo.controllers.api.sca.v1.model.CreateConnectionResponseData
import com.saltedge.provider.demo.errors.BadRequest
import com.saltedge.provider.demo.model.ScaConnectionEntity
import com.saltedge.provider.demo.model.ScaConnectionsRepository
import com.saltedge.provider.demo.tools.security.CryptoTools
import com.saltedge.provider.demo.tools.security.KeyTools
import com.saltedge.provider.demo.tools.toJson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
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
        val privateDhKey = applicationProperties.privateDhKey
        val authPublicDhKey = KeyTools.convertPemToPublicKey(request.data.publicKey, KeyTools.Algorithm.DIFFIE_HELLMAN) ?: throw BadRequest.WrongRequestFormat(errorMessage = "invalid public key")
        val sharedSecret: SecretKey = KeyTools.computeSecretKey(privateDhKey, authPublicDhKey)
        val accessToken = UUID.randomUUID().toString()
        val accessTokenResponseJson = AccessTokenResponse(accessToken).toJson() ?: ""
        val encryptedJson = CryptoTools.encryptAes(accessTokenResponseJson, sharedSecret) ?: ""

        val entity = ScaConnectionEntity()
        entity.connectionId = request.data.connectionId
        entity.publicKey = request.data.publicKey
        entity.returnUrl = request.data.returnUrl
        entity.accessToken = accessToken
        connectionsRepository.save(entity)

        val authenticationUrl = "${request.data.returnUrl}?access_token=$encryptedJson"
        return ResponseEntity(CreateConnectionResponse(data = CreateConnectionResponseData(authenticationUrl = authenticationUrl)), HttpStatus.OK)
    }
}
