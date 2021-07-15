/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.controllers.api.sca.v1

import com.saltedge.provider.demo.config.SCA_USER_ID
import com.saltedge.provider.demo.controllers.api.sca.v1.model.*
import com.saltedge.provider.demo.errors.BadRequest
import com.saltedge.provider.demo.errors.NotFound
import com.saltedge.provider.demo.model.ScaActionsRepository
import com.saltedge.provider.demo.model.ScaConnectionsRepository
import com.saltedge.provider.demo.tools.createAuthorizationData
import com.saltedge.provider.demo.tools.createEncryptedAction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(ActionsController.BASE_PATH)
class ActionsController : BaseController() {
    companion object {
        const val BASE_PATH: String = "$API_BASE_PATH/actions"
    }

    @Autowired
    lateinit var actionsRepository: ScaActionsRepository
    @Autowired
    lateinit var connectionsRepository: ScaConnectionsRepository

    @Throws(Exception::class)
    @PostMapping("/{action_id}")
    fun createAuthorizationInfo(
        @PathVariable("action_id") actionId: Long,
        @RequestBody request: CreateAuthorizationInfoRequest
    ): ResponseEntity<CreateAuthorizationInfoResponse> {
        val action = actionsRepository.findById(actionId).orElse(null) ?: throw NotFound.ActionNotFound()
        val connectionIdValue = request.data.connectionId ?: throw BadRequest.WrongRequestFormat()
        val connection = connectionsRepository.findFirstByConnectionId(connectionIdValue)    ?: throw NotFound.ConnectionNotFound()

        val authorization = createAuthorizationData(action)
        val encAuthorization = createEncryptedAction(data = authorization, connection = connection)

        return ResponseEntity(CreateAuthorizationInfoResponse(data = CreateAuthorizationInfoResponseData(
            action_id = actionId.toString(),
            user_id = SCA_USER_ID,
            expires_at = action.expiresAt.toString(),
            authorization = encAuthorization
        )), HttpStatus.OK)
    }

    @Throws(Exception::class)
    @PutMapping("/{action_id}/confirm")
    fun confirm(
        @PathVariable("action_id") actionId: Long,
        @RequestBody request: UpdateActionRequest
    ): ResponseEntity<UpdateActionResponse> {
        val action = actionsRepository.findById(actionId).orElse(null) ?: throw NotFound.ActionNotFound()

        if (action.status == "confirmed" || action.status == "denied") throw BadRequest.ActionClosed()

        action.status = "confirmed"
        actionsRepository.save(action)
        return ResponseEntity(UpdateActionResponse(data = UpdateActionResponseData(close_action = true)), HttpStatus.OK)
    }

    @PutMapping("/{action_id}/deny")
    fun deny(
        @PathVariable("action_id") actionId: Long,
        @RequestBody request: UpdateActionRequest
    ): ResponseEntity<UpdateActionResponse> {
        val action = actionsRepository.findById(actionId).orElse(null) ?: throw NotFound.ActionNotFound()

        if (action.status == "confirmed" || action.status == "denied") throw BadRequest.ActionClosed()

        action.status = "denied"
        actionsRepository.save(action)
        return ResponseEntity(UpdateActionResponse(data = UpdateActionResponseData(close_action = true)), HttpStatus.OK)
    }
}