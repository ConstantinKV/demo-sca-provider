/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.controllers.api.sca.v1.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.saltedge.provider.demo.callback.ActionAuthorization

data class CreateAuthorizationInfoResponse(
    @JsonProperty("data") var data: CreateAuthorizationInfoResponseData
)

data class CreateAuthorizationInfoResponseData(
    @JsonProperty("action_id") var action_id: String,
    @JsonProperty("user_id") var user_id: String,
    @JsonProperty("expires_at") var expires_at: String,
    @JsonProperty("authorizations") var authorization: ActionAuthorization
)
