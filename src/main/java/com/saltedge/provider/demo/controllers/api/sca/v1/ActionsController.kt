/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.controllers.api.sca.v1

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(ActionsController.BASE_PATH)
class ActionsController : BaseController() {
    companion object {
        const val BASE_PATH: String = "$API_BASE_PATH/actions"
    }

}