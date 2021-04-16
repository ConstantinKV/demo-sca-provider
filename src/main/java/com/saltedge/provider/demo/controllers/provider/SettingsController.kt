/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.controllers.provider

import com.saltedge.provider.demo.config.ApplicationProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView

@Controller
class SettingsController {

    @Autowired
    lateinit var applicationProperties: ApplicationProperties

    @GetMapping("/settings")
    fun showSettings(): ModelAndView {
        return ModelAndView("settings")
            .addObject("sca_service_url", applicationProperties.scaServiceUrl)
            .addObject("provider_id", applicationProperties.providerId)
    }

    @PostMapping("/settings")
    fun submitSettings(
        @RequestParam("sca_service_url") scaServiceUrl: String,
        @RequestParam("provider_id") providerId: String,
    ): ModelAndView {
        if (scaServiceUrl.isNotBlank() && scaServiceUrl != applicationProperties.scaServiceUrl) {
            applicationProperties.scaServiceUrl = scaServiceUrl
        }
        if (providerId.isNotBlank() && providerId != applicationProperties.providerId) {
            applicationProperties.providerId = providerId
        }
        println("submitSettings scaServiceUrl:$scaServiceUrl [${applicationProperties.scaServiceUrl}]")
        println("submitSettings providerId:$providerId [${applicationProperties.providerId}]")
        return ModelAndView("settings")
            .addObject("sca_service_url", applicationProperties.scaServiceUrl)
            .addObject("provider_id", applicationProperties.providerId)
    }
}
