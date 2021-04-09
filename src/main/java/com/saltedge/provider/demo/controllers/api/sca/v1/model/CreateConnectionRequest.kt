/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.controllers.api.sca.v1.model

import com.fasterxml.jackson.annotation.JsonProperty

data class CreateConnectionRequest(
    @JsonProperty("data") var data: CreateConnectionRequestData,
    @JsonProperty("exp") var exp: Integer
)

data class CreateConnectionRequestData(
    @JsonProperty("connection_id") var connectionId: String,
    @JsonProperty("public_key") var publicKey: String,
    @JsonProperty("return_url") var returnUrl: String,
    @JsonProperty("connect_query") var connectQuery: String?
)
