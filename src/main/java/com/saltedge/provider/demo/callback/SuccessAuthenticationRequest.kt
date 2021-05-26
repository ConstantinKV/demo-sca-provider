/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.callback

import com.fasterxml.jackson.annotation.JsonProperty

data class SuccessAuthenticationRequest(
    @JsonProperty("data") var data: SuccessAuthenticationRequestData,
    @JsonProperty("exp") var exp: Int
)

data class SuccessAuthenticationRequestData(
    @JsonProperty("user_id") var user_id: String,
    @JsonProperty("access_token") var access_token: String,
    @JsonProperty("rsa_public_key") var rsa_public_key: String
)