/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.callback

import com.fasterxml.jackson.annotation.JsonProperty

data class AccessTokenWrapper(
    @JsonProperty("access_token") var access_token: String,
)
