/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.controllers.provider

import com.saltedge.provider.demo.controllers.api.sca.v1.ConnectionsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.util.UriComponentsBuilder

@Controller
class AuthController {
    companion object {
        const val BASE_PATH: String = "/auth"
        const val KEY_SCA_CONNECTION_ID: String = "sca_connection_id"
        const val KEY_CONNECT_QUERY: String = "connect_query"

        fun authenticationPageUrl(applicationUrl: String, connectionId: String, connectQuery: String): String {
            return UriComponentsBuilder.fromUriString(applicationUrl).path(BASE_PATH)
                .queryParam(KEY_SCA_CONNECTION_ID, connectionId)
                .queryParam(KEY_CONNECT_QUERY, connectQuery)
                .build().toUriString()
        }
    }

    @Autowired
    lateinit var connectionsService: ConnectionsService

    @GetMapping(BASE_PATH)
    fun showAuthenticationPage(
        @RequestParam(name = KEY_SCA_CONNECTION_ID) scaConnectionId: String,
        @RequestParam(name = KEY_CONNECT_QUERY) connectQuery: String
    ): ModelAndView {
        val redirectUrl = connectionsService.authorizeConnection(scaConnectionId, connectQuery)
        return ModelAndView("redirect:$redirectUrl")
    }
}
