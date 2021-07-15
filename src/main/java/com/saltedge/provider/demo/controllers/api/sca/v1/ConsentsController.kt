/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.controllers.api.sca.v1

import com.saltedge.provider.demo.config.DemoApplicationProperties
import com.saltedge.provider.demo.controllers.api.sca.v1.model.EmptyRequest
import com.saltedge.provider.demo.controllers.api.sca.v1.model.EmptyResponse
import com.saltedge.provider.demo.errors.BadRequest
import com.saltedge.provider.demo.errors.NotFound
import com.saltedge.provider.demo.model.ScaConsentsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController//https://demo-sca-provider.herokuapp.com
@RequestMapping(ConsentsController.BASE_PATH)
class ConsentsController : BaseController() {
    companion object {
        const val BASE_PATH: String = "$API_BASE_PATH/consents"
    }
    @Autowired
    lateinit var demoApplicationProperties: DemoApplicationProperties
    @Autowired
    lateinit var repository: ScaConsentsRepository

    @PutMapping("/{consent_id}/revoke")
    fun revoke(
        @PathVariable("consent_id") consentId: String,
        @RequestBody request: EmptyRequest
    ): ResponseEntity<EmptyResponse> {
        try {
            val consentIdValue = consentId.toLongOrNull() ?: throw NotFound.ConsentNotFound()
            repository.findById(consentIdValue).orElse(null)?.let {
                it.revoked = true
                repository.save(it)
            } ?: throw NotFound.ConsentNotFound()
            return ResponseEntity(EmptyResponse(), HttpStatus.OK)
        } catch (e: Exception) {
            println(e.message)
            e.printStackTrace()
            if (e is NotFound || e is BadRequest) throw e
            else throw BadRequest.WrongRequestFormat(errorMessage = "Internal error")
        }
    }
}
