/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.controllers.api.sca.v1.model

import com.fasterxml.jackson.annotation.JsonProperty

data class UpdateConnectionRequest(
    @JsonProperty("data") var data: Any?,
    @JsonProperty("exp") var exp: Integer
)
