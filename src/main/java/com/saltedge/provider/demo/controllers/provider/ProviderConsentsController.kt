/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.controllers.provider

import com.saltedge.provider.demo.callback.ConsentAccount
import com.saltedge.provider.demo.callback.ScaServiceCallback
import com.saltedge.provider.demo.config.DemoApplicationProperties
import com.saltedge.provider.demo.model.ScaConnectionsRepository
import com.saltedge.provider.demo.model.ScaConsentEntity
import com.saltedge.provider.demo.model.ScaConsentsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import java.time.Instant
import java.time.temporal.ChronoUnit

@Controller
class ProviderConsentsController {

    @Autowired
    lateinit var demoApplicationProperties: DemoApplicationProperties
    @Autowired
    lateinit var connectionsRepository: ScaConnectionsRepository
    @Autowired
    lateinit var repository: ScaConsentsRepository
    @Autowired
    lateinit var callbackService: ScaServiceCallback

    @GetMapping("/consents")
    fun showConnections(): ModelAndView {
        val consents = repository.findAll(Sort.by(Sort.Direction.DESC, "id"))
        return ModelAndView("consents").addObject("consents", consents)
    }

    @GetMapping("/consents/{consent_id}/revoke")
    fun revokeConsent(@PathVariable("consent_id") consentId: Long): ModelAndView {
        repository.findById(consentId).orElse(null)?.let {
            it.revoked = true
            repository.save(it)
            callbackService.sendRevokeConsentCallback(consentId.toString())
        }
        return ModelAndView("redirect:/consents")
    }

    /**
     * type: aisp, pisp_future, pisp_recurring
     */
    @GetMapping("/consents/create")
    fun createConsent(@RequestParam(name = "user_id") userId: String): ModelAndView {
        val connections = connectionsRepository.findByUserId(userId)
        return ModelAndView("create_consent").addObject("active", connections.isNotEmpty())
    }

    @PostMapping("/consents/create")
    fun createConsent(
        @RequestParam(name = "user_id") userId: String,
        @RequestParam("tpp_name") tppName: String,
        @RequestParam("type_group", required = false) type: String?,
        @RequestParam("balance", required = false) balance: String?,
        @RequestParam("transactions", required = false) transactions: String?
    ): ModelAndView {
        val connections = connectionsRepository.findByUserId(userId)
        if (connections.isNotEmpty()) {
            val consent = ScaConsentEntity()
            consent.userId = connections.first().userId
            consent.consentType = type ?: "aisp"
            consent.tppName = tppName.ifEmpty { "Fentury" }

            val consentAccount = ConsentAccount(name = "Checking account $userId", iban = "RO49AAAA1B31007593840000")
            consent.setConsentAccounts(listOf(consentAccount))

            consent.balance = balance != null
            consent.transactions = transactions != null
            consent.expiresAt = Instant.now().plus(90, ChronoUnit.DAYS)

            repository.save(consent)
            callbackService.sendConsentCreateCallback(consent, connections)
        }
        return ModelAndView("redirect:/connections")
    }
}
