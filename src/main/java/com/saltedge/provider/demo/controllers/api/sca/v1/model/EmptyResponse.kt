/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.controllers.api.sca.v1.model

import com.fasterxml.jackson.annotation.JsonProperty

data class EmptyResponse(
    @JsonProperty("data") var data: Any = Any()
)
