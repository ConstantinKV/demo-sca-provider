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
    @JsonProperty("action_id") var action_id: String,
    @JsonProperty("user_id") var user_id: String,
    @JsonProperty("expires_at") var expires_at: String,
    @JsonProperty("authorizations") var authorizations: List<ActionAuthorization>
)

data class ActionAuthorization(
    @JsonProperty("connection_id") var connection_id: String,
    @JsonProperty("iv") var iv: String,
    @JsonProperty("key") var key: String,
    @JsonProperty("data") var data: String
)

data class AuthorizationData(
    @JsonProperty("title") var title: String,
    @JsonProperty("description") var description: DescriptionData,
    @JsonProperty("authorization_code") var authorization_code: String,
    @JsonProperty("created_at") var created_at: String,
    @JsonProperty("expires_at") var expires_at: String
)

data class DescriptionData(
    val payment: DescriptionPaymentData? = null,
    val text: String? = null,
    val html: String? = null,
    var extra: ExtraData? = null
)

data class DescriptionPaymentData(
    var payee: String? = null,
    var amount: String? = null,
    var account: String? = null,
    var payment_date: String? = null,
    var reference: String? = null,
    var fee: String? = null,
    var exchange_rate: String? = null
)

data class ExtraData(
    var action_date: String,
    var device: String,
    var location: String,
    var ip: String
)
