/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.controllers.api.sca.v1

import com.saltedge.provider.demo.config.DemoApplicationProperties
import com.saltedge.provider.demo.controllers.api.sca.v1.model.CreateConnectionRequest
import com.saltedge.provider.demo.controllers.api.sca.v1.model.CreateConnectionResponse
import com.saltedge.provider.demo.controllers.api.sca.v1.model.EmptyRequest
import com.saltedge.provider.demo.controllers.api.sca.v1.model.EmptyResponse
import com.saltedge.provider.demo.errors.BadRequest
import com.saltedge.provider.demo.errors.NotFound
import com.saltedge.provider.demo.tools.security.CryptoTools
import com.saltedge.provider.demo.tools.security.KeyTools
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController//https://demo-sca-provider.herokuapp.com
@RequestMapping(ConnectionsController.BASE_PATH)
class ConnectionsController : BaseController() {
    companion object {
        const val BASE_PATH: String = "$API_BASE_PATH/connections"
    }
    @Autowired
    lateinit var demoApplicationProperties: DemoApplicationProperties
    @Autowired
    lateinit var connectionsService: ConnectionsService

    @PostMapping
    fun create(@RequestBody request: CreateConnectionRequest): ResponseEntity<CreateConnectionResponse> {
        try {
            val rsaPrivateKey = demoApplicationProperties.rsaPrivateKey
            val authRsaPublicKeyPem = CryptoTools.decryptPublicRsaKey(request.data.encRsaPublicKey, rsaPrivateKey)
            KeyTools.convertPemToPublicKey(authRsaPublicKeyPem, KeyTools.Algorithm.RSA)
                ?: throw BadRequest.WrongRequestFormat(errorMessage = "invalid rsa public key")
            val responseData = connectionsService.processConnectRequest(request.data, authRsaPublicKeyPem)
            return ResponseEntity(CreateConnectionResponse(data = responseData), HttpStatus.OK)
        } catch (e: Exception) {
            println(e.message)
            e.printStackTrace()
            if (e is NotFound || e is BadRequest) throw e
            else throw BadRequest.WrongRequestFormat(errorMessage = "Internal error")
        }
    }

    @PutMapping("/{connection_id}/revoke")
    fun revoke(
        @PathVariable("connection_id") connectionId: String,
        @RequestBody request: EmptyRequest
    ): ResponseEntity<EmptyResponse> {
        try {
            connectionsService.revokeConnection(connectionId)
            return ResponseEntity(EmptyResponse(), HttpStatus.OK)
        } catch (e: Exception) {
            println(e.message)
            e.printStackTrace()
            if (e is NotFound || e is BadRequest) throw e
            else throw BadRequest.WrongRequestFormat(errorMessage = "Internal error")
        }
    }
}
