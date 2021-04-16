/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.controllers.api.sca.v1

import com.saltedge.provider.demo.controllers.api.sca.v1.model.UpdateActionRequest
import com.saltedge.provider.demo.controllers.api.sca.v1.model.UpdateActionResponse
import com.saltedge.provider.demo.controllers.api.sca.v1.model.UpdateActionResponseData
import com.saltedge.provider.demo.errors.NotFound
import com.saltedge.provider.demo.model.ScaActionsRepository
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
    lateinit var repository: ScaActionsRepository

    @PutMapping("/{action_id}/confirm")
    fun confirm(
        @PathVariable("action_id") actionId: Long,
        @RequestBody request: UpdateActionRequest
    ): ResponseEntity<UpdateActionResponse> {
        val action = repository.findById(actionId).orElseThrow() ?: throw NotFound.ActionNotFound()
        action.status = "confirmed"
        repository.save(action)
        return ResponseEntity(UpdateActionResponse(data = UpdateActionResponseData(closeAction = true)), HttpStatus.OK)
    }

    @PutMapping("/{action_id}/deny")
    fun deny(
        @PathVariable("action_id") actionId: Long,
        @RequestBody request: UpdateActionRequest
    ): ResponseEntity<UpdateActionResponse> {
        val action = repository.findById(actionId).orElseThrow() ?: throw NotFound.ActionNotFound()
        action.status = "denied"
        repository.save(action)
        return ResponseEntity(UpdateActionResponse(data = UpdateActionResponseData(closeAction = true)), HttpStatus.OK)
    }
}