/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.controllers.api.sca.v1.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CreateConnectionResponse(
    @JsonProperty("data") var data: CreateConnectionResponseData
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CreateConnectionResponseData(
    @JsonProperty("authentication_url") var authenticationUrl: String,
    @JsonProperty("user_id") var userId: String? = null,
    @JsonProperty("access_token") var accessToken: String? = null,
    @JsonProperty("rsa_public_key") var rsaPublicKey: String? = null
)
