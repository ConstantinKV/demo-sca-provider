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
    lateinit var properties: DemoApplicationProperties

    @GetMapping("/settings")
    fun showSettings(): ModelAndView {
        return ModelAndView("settings")
            .addObject("sca_url", properties.scaServiceUrl)
            .addObject("provider_id", properties.scaProviderId)
            .addObject("sca_rsa_key", properties.scaServiceRsaPublicKeyPem)
            .addObject("app_url", properties.applicationUrl)
            .addObject("sca_key_file", properties.scaServicePublicRsaKeyFile)
    }

    @PostMapping("/settings")
    fun submitSettings(
        @RequestParam("sca_url") scaServiceUrl: String,
        @RequestParam("provider_id") providerId: String,
        @RequestParam("sca_rsa_key") scaServiceRsaPublicKey: String
    ): ModelAndView {
        if (scaServiceUrl.isNotBlank() && scaServiceUrl != properties.scaServiceUrl) {
            properties.scaServiceUrl = scaServiceUrl
        }
        if (providerId.isNotBlank() && providerId != properties.scaProviderId) {
            properties.scaProviderId = providerId
        }
        if (scaServiceRsaPublicKey != properties.scaServiceRsaPublicKeyPem) {
            properties.scaServiceRsaPublicKeyPem = providerId
        }
        return ModelAndView("redirect:/settings")
    }
}
