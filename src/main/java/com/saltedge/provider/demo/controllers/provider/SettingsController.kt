/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.controllers.provider

import com.saltedge.provider.demo.config.DemoApplicationProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView

@Controller
class SettingsController {

    @Autowired
    lateinit var propertiesDemo: DemoApplicationProperties

    @GetMapping("/settings")
    fun showSettings(): ModelAndView {
        return ModelAndView("settings")
            .addObject("sca_url", propertiesDemo.scaServiceUrl)
            .addObject("provider_id", propertiesDemo.scaProviderId)
            .addObject("sca_rsa_key", propertiesDemo.scaServiceRsaPublicKeyPem)
    }

    @PostMapping("/settings")
    fun submitSettings(
        @RequestParam("sca_url") scaServiceUrl: String,
        @RequestParam("provider_id") providerId: String,
        @RequestParam("sca_rsa_key") scaServiceRsaPublicKey: String
    ): ModelAndView {
        if (scaServiceUrl.isNotBlank() && scaServiceUrl != propertiesDemo.scaServiceUrl) {
            propertiesDemo.scaServiceUrl = scaServiceUrl
        }
        if (providerId.isNotBlank() && providerId != propertiesDemo.scaProviderId) {
            propertiesDemo.scaProviderId = providerId
        }
        if (scaServiceRsaPublicKey != propertiesDemo.scaServiceRsaPublicKeyPem) {
            propertiesDemo.scaServiceRsaPublicKeyPem = providerId
        }
        return ModelAndView("redirect:/settings")
    }
}
