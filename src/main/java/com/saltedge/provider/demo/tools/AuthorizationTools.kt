package com.saltedge.provider.demo.tools

import com.saltedge.provider.demo.callback.*
import com.saltedge.provider.demo.model.ScaActionEntity
import com.saltedge.provider.demo.model.ScaConnectionEntity
import com.saltedge.provider.demo.tools.security.CryptoTools
import com.saltedge.provider.demo.tools.security.KeyTools
import java.security.SecureRandom
import java.util.*

fun createAuthorizationData(action: ScaActionEntity): String {
    val description = when (action.descriptionType) {
        "html" -> DescriptionData(html = "<body><p><b>TPP</b> is requesting your authorization to access account information data from <b>Demo Bank</b></p></body>")
        "json" -> {
            DescriptionData(payment = DescriptionPaymentData(
                payee = "TPP",
                amount = "100.0",
                account = "MD24 AG00 0225 1000 1310 4168",
                payment_date = action.createdAtDescription,
                reference = "X1",
                fee = "No fee",
                exchange_rate = "1.0"
            )
            )
        }
        else -> DescriptionData(text = "TPP is requesting your authorization to access account information data from Demo Bank")
    }
    description.extra = ExtraData(action_date = "Today", device = "Google Chrome", location = "Munich, Germany", ip = "127.0.0.0")
    val authorizationData = AuthorizationData(
        title = "Access account information",
        description = description,
        authorization_code = action.code,
        created_at = action.createdAtValue.toString(),
        expires_at = action.expiresAt.toString()
    )
    return authorizationData.toJson() ?: ""
}

/**
 * AES-256-CBC
 */
fun createEncryptedEntity(data: String, connection: ScaConnectionEntity): ActionAuthorization {
    val publicKey = KeyTools.convertPemToPublicKey(connection.rsaPublicKey, KeyTools.Algorithm.RSA)

    val key = ByteArray(32)
    SecureRandom().nextBytes(key)
    val iv = ByteArray(16)
    SecureRandom().nextBytes(iv)

    return ActionAuthorization(
        connection_id = connection.connectionId,
        key = Base64.getEncoder().encodeToString(CryptoTools.encryptRsa(key, publicKey)),
        iv = Base64.getEncoder().encodeToString(CryptoTools.encryptRsa(iv, publicKey)),
        data = Base64.getEncoder().encodeToString(CryptoTools.encryptAes(data, key, iv))
    )
}