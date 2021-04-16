/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.controllers.api.sca.v1.model

import com.fasterxml.jackson.annotation.JsonProperty

data class UpdateActionRequest(
    @JsonProperty("data") var data: UpdateActionRequestData,
    @JsonProperty("exp") var exp: Integer
)

data class UpdateActionRequestData(
    @JsonProperty("connection_id") var data: String,
    @JsonProperty("original_request_jws_signature") var originalRequestJwsSignature: String,
    @JsonProperty("original_request") var originalRequest: String
)
