/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.callback

import com.fasterxml.jackson.annotation.JsonProperty

data class RevokeConnectionRequest(
    @JsonProperty("data") var data: Any = Any(),
    @JsonProperty("exp") var exp: Int
)
