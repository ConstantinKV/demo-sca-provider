/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.controllers.api.sca.v1.model

import com.fasterxml.jackson.annotation.JsonProperty

data class UpdateActionResponse(
    @JsonProperty("data") var data: UpdateActionResponseData
)

data class UpdateActionResponseData(
    @JsonProperty("close_action") var closeAction: Boolean
)
