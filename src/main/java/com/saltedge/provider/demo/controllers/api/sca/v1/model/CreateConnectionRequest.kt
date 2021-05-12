/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.controllers.api.sca.v1.model

import com.fasterxml.jackson.annotation.JsonProperty

data class CreateConnectionRequest(
    @JsonProperty("data") var data: CreateConnectionRequestData,
    @JsonProperty("exp") var exp: Integer
)

data class CreateConnectionRequestData(
    @JsonProperty("connection_id") var connectionId: String,
    @JsonProperty("return_url") var returnUrl: String,
    @JsonProperty("enc_rsa_public_key") var encRsaPublicKey: EncryptedEntity,
    @JsonProperty("connect_query") var connectQuery: String?
)

data class EncryptedEntity(
    @JsonProperty("key") var encryptedKey: String,
    @JsonProperty("iv") var encryptedIv: String,
    @JsonProperty("data") var encryptedData: String
)
