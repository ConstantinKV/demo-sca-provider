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
    @JsonProperty("user_id") var userId: String,
    @JsonProperty("access_token") var accessToken: String,
    @JsonProperty("rsa_public_key") var rsaPublicKey: String
)