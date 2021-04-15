/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.callback

import com.fasterxml.jackson.annotation.JsonProperty

data class CreateActionRequest(
    @JsonProperty("data") var data: CreateActionRequestData,
    @JsonProperty("exp") var exp: Int
)

data class CreateActionRequestData(
    @JsonProperty("action_id") var actionId: String,
    @JsonProperty("user_id") var userId: String,
    @JsonProperty("expires_at") var expiresAt: String,
    @JsonProperty("authorizations") var authorizations: List<CreateActionAuthorization>
)

data class CreateActionAuthorization(
    @JsonProperty("connection_id") var connectionId: String,
    @JsonProperty("iv") var iv: String,
    @JsonProperty("key") var key: String,
    @JsonProperty("data") var data: String
)

data class AuthorizationData(
    @JsonProperty("title") var title: String,
    @JsonProperty("description") var description: DescriptionData,
    @JsonProperty("extra") var extra: ExtraData,
    @JsonProperty("authorization_code") var authorizationCode: String,
    @JsonProperty("created_at") var createdAt: String,
    @JsonProperty("expires_at") var expiresAt: String
)

data class DescriptionData(
    @JsonProperty("text") var text: String
)

data class ExtraData(
    @JsonProperty("action_date") var actionDate: String,
    @JsonProperty("device") var device: String,
    @JsonProperty("location") var location: String,
    @JsonProperty("ip") var ip: String
)
