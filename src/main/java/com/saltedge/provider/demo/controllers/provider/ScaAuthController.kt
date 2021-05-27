/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.controllers.provider

import com.saltedge.provider.demo.config.SCA_CONNECT_QUERY_PREFIX
import com.saltedge.provider.demo.config.SCA_USER_ID
import com.saltedge.provider.demo.config.SCA_USER_NAME
import com.saltedge.provider.demo.config.SCA_USER_PASSWORD
import com.saltedge.provider.demo.controllers.api.sca.v1.ConnectionsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.util.UriComponentsBuilder

@Controller
class ScaAuthController {
    companion object {
        const val TEMPLATE: String = "auth"
        const val BASE_PATH: String = "/$TEMPLATE"
        const val KEY_SCA_CONNECTION_ID: String = "sca_connection_id"
        const val KEY_CONNECT_QUERY: String = "connect_query"

        fun authenticationPageUrl(applicationUrl: String, connectionId: String, connectQuery: String? = null): String {
            val builder = UriComponentsBuilder.fromUriString(applicationUrl)
                .path(BASE_PATH)
                .queryParam(KEY_SCA_CONNECTION_ID, connectionId)
            connectQuery?.let { builder.queryParam(KEY_CONNECT_QUERY, it) }
            return builder.build().toUriString()
        }
    }

    @Autowired
    lateinit var connectionsService: ConnectionsService

    @GetMapping(BASE_PATH)
    fun showAuthenticationPage(
        @RequestParam(name = KEY_SCA_CONNECTION_ID, required = false) scaConnectionId: String?,
        @RequestParam(name = KEY_CONNECT_QUERY, required = false) connectQuery: String?
    ): ModelAndView {
        return when {
            scaConnectionId == null -> {
                ModelAndView(TEMPLATE)
                    .addObject(KEY_SCA_CONNECTION_ID, "")
                    .addObject("error", "Invalid environment. Authenticator is required.")
            }
            connectQuery == null -> {
                ModelAndView(TEMPLATE).addObject(KEY_SCA_CONNECTION_ID, scaConnectionId)
            }
            else -> {
                val userId = connectQuery.replace(SCA_CONNECT_QUERY_PREFIX, "")
                ModelAndView("redirect:${connectionsService.authorizeConnection(scaConnectionId, userId)}")
            }
        }
    }

    @PostMapping(BASE_PATH)
    fun postAuthenticationParams(
        @RequestParam(required = true) username: String,
        @RequestParam(required = true) password: String,
        @RequestParam(name = KEY_SCA_CONNECTION_ID, required = true) scaConnectionId: String
    ): ModelAndView {
        println("postAuthenticationParams: [$username/$password] for connection $scaConnectionId")
        return if (username == SCA_USER_NAME && password == SCA_USER_PASSWORD) {
            ModelAndView("redirect:${connectionsService.authorizeConnection(scaConnectionId, SCA_USER_ID)}")
        } else {
            ModelAndView(TEMPLATE)
                .addObject(KEY_SCA_CONNECTION_ID, scaConnectionId)
                .addObject("error", "Invalid credentials")
        }
    }
}
