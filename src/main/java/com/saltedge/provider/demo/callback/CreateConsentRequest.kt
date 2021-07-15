/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.callback

import com.fasterxml.jackson.annotation.JsonProperty

data class CreateConsentRequest(
    @JsonProperty("data") var data: CreateConsentRequestData,
    @JsonProperty("exp") var exp: Int
)

data class CreateConsentRequestData(
    @JsonProperty("consent_id") var consent_id: String,
    @JsonProperty("user_id") var user_id: String,
    @JsonProperty("consents") var consents: List<EncryptedConsent>
)

data class EncryptedConsent(
    @JsonProperty("connection_id") var connection_id: String,
    @JsonProperty("expires_at") var expires_at: String,
    @JsonProperty("iv") var iv: String,
    @JsonProperty("key") var key: String,
    @JsonProperty("data") var data: String
)

data class ConsentData(
    @JsonProperty("id") var id: String,
    @JsonProperty("consent_type") var consent_type: String,
    @JsonProperty("tpp_name") var tpp_name: String,
    @JsonProperty("accounts") var accounts: List<ConsentAccount>,
    @JsonProperty("shared_data") var shared_data: ConsentSharedData,
    @JsonProperty("created_at") var created_at: String,
    @JsonProperty("expires_at") var expires_at: String
)

data class ConsentAccount(
    val name: String,
    val account_number: String? = null,
    val sort_code: String? = null,
    val iban: String? = null
)

data class ConsentSharedData(
    val balance: Boolean,
    val transactions: Boolean
)
