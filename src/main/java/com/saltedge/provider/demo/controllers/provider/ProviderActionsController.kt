/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.controllers.provider

import com.saltedge.provider.demo.callback.ScaServiceCallback
import com.saltedge.provider.demo.model.ScaActionEntity
import com.saltedge.provider.demo.model.ScaActionsRepository
import com.saltedge.provider.demo.model.ScaConnectionsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.servlet.ModelAndView
import java.util.*

@Controller
class ProviderActionsController {

    @Autowired
    lateinit var actionsRepository: ScaActionsRepository
    @Autowired
    lateinit var connectionsRepository: ScaConnectionsRepository
    @Autowired
    lateinit var callbackService: ScaServiceCallback

    @GetMapping("/actions")
    fun showActions(): ModelAndView {
        val connections = connectionsRepository.findByRevokedIsFalse().filter { it.isAuthorized }
        val actions = actionsRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
        return ModelAndView("actions")
            .addObject("actions", actions)
            .addObject("disabled", if (connections.isEmpty()) "disabled" else "")
    }

    @GetMapping("/actions/create")
    fun createNewAction(): ModelAndView {
        val connections = connectionsRepository.findByRevokedIsFalse().filter { it.isAuthorized }
        if (connections.isNotEmpty()) {
            val action = ScaActionEntity()
            action.code = UUID.randomUUID().toString()
            action.status = "pending"
            actionsRepository.save(action)

            callbackService.sendActionCreateCallback(action, connections)
        }
        return ModelAndView("redirect:/actions")
    }

    @GetMapping("/actions/{action_id}/close")
    fun closeAction(@PathVariable("action_id") actionId: Long): ModelAndView {
        actionsRepository.findById(actionId).orElse(null)?.let {
            it.status = "closed"
            actionsRepository.save(it)
        }
        return ModelAndView("redirect:/actions")
    }
}
