/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.callback

import com.fasterxml.jackson.annotation.JsonProperty

data class FailAuthenticationRequest(
    @JsonProperty("data") var data: FailAuthenticationRequestData,
    @JsonProperty("exp") var exp: Int
)

data class FailAuthenticationRequestData(
    @JsonProperty("fail_message") var failMessage: String
)