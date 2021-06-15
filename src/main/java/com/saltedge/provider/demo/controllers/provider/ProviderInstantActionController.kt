/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.controllers.provider

import com.saltedge.provider.demo.config.DemoApplicationProperties
import com.saltedge.provider.demo.model.ScaActionEntity
import com.saltedge.provider.demo.model.ScaActionsRepository
import com.saltedge.provider.demo.model.ScaConnectionsRepository
import com.saltedge.provider.demo.tools.COOKIE_AUTHENTICATION_ACTION
import com.saltedge.provider.demo.tools.QrTools
import com.saltedge.provider.demo.tools.saveActionCookie
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.util.UriComponentsBuilder
import java.util.*
import javax.servlet.http.HttpServletResponse

@Controller
class ProviderInstantActionController {

    @Autowired
    lateinit var propertiesDemo: DemoApplicationProperties
    @Autowired
    lateinit var actionsRepository: ScaActionsRepository
    @Autowired
    lateinit var connectionsRepository: ScaConnectionsRepository

    /**
     * http://localhost:8080/instant_action
     */
    @GetMapping("/instant_action")
    fun showInstantAction(
        @CookieValue(value = COOKIE_AUTHENTICATION_ACTION, defaultValue = "") savedActionId: String,
        response: HttpServletResponse
    ): ModelAndView {
        val action = getOrCreateAction(savedActionId)
        val actionId = action.id?.toString() ?: ""
        if (savedActionId != actionId) saveActionCookie(COOKIE_AUTHENTICATION_ACTION, actionId, response)

        val providerId = propertiesDemo.scaProviderId

        val builder = UriComponentsBuilder.fromUriString(propertiesDemo.applicationUrl)
            .path("/instant_action/qr")
            .queryParam("action_id", actionId)
            .queryParam("provider_id", providerId)

        return ModelAndView("instant_action")
            .addObject("action_id", actionId)
            .addObject("app_link", createAppLink(actionId, providerId))
            .addObject("qr_link", builder.build().toUriString())
    }

    @GetMapping("/instant_action/status")
    @ResponseBody
    fun showInstantActionStatus(
        @RequestParam(name = "action_id", required = false) actionId: String?): Map<String, String> {
        val action = actionId?.toLongOrNull()?.let { actionsRepository.findByIdOrNull(it) }
        val status = action?.status?.toLowerCase() ?: "No action found"
        return mapOf("status" to status)
    }

    /**
     * http://localhost:8080/instant_action/qr?action_id=123&provider_id=777
     */
    @GetMapping("/instant_action/qr")
    fun connectQRImage(
        response: HttpServletResponse,
        @RequestParam(name = "action_id", required = true) actionId: String,
        @RequestParam(name = "provider_id", required = true) providerId: String
    ) {
        val appLink: String = createAppLink(actionId, providerId)
        QrTools.encodeTextAsQrPngImage(appLink, 256, 256)?.let { image ->
            response.contentType = "image/png"
            val outputStream = response.outputStream
            outputStream.write(image)
            outputStream.flush()
            outputStream.close()
        }
    }

    private fun createAppLink(actionId: String, providerId: String): String {
        return "authenticator://saltedge.com/action?api_version=2&action_id=$actionId&provider_id=$providerId"
    }

    private fun getOrCreateAction(actionId: String): ScaActionEntity {
        var action = actionId.toLongOrNull()?.let { actionsRepository.findByIdOrNull(it) }
        if (action == null) {
            action = ScaActionEntity().apply {
                code = UUID.randomUUID().toString()
                status = "pending"
                descriptionType = "text"
            }
            actionsRepository.save(action)
        }
        return action
    }
}
