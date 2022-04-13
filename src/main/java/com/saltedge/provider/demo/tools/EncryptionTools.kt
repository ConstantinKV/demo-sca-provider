package com.saltedge.provider.demo.tools

import com.saltedge.provider.demo.callback.*
import com.saltedge.provider.demo.model.ScaActionEntity
import com.saltedge.provider.demo.model.ScaConnectionEntity
import com.saltedge.provider.demo.model.ScaConsentEntity
import com.saltedge.provider.demo.tools.security.CryptoTools
import com.saltedge.provider.demo.tools.security.KeyTools
import java.security.SecureRandom
import java.time.Instant
import java.util.*

/**
 * return AuthorizationData as json string
 */
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
 * return ConsentData as json string
 */
fun createConsentData(model: ScaConsentEntity): String {
    val data = ConsentData(
        id = model.id.toString(),
        consent_type = model.consentType,
        tpp_name = model.tppName,
        accounts = model.getConsentAccounts(),
        shared_data = ConsentSharedData(balance = model.balance, transactions = model.transactions),
        created_at = (model.createdAt ?: Instant.ofEpochSecond(0)).toString(),
        expires_at = model.expiresAt.toString()
    )
    return data.toJson() ?: ""
}

/**
 * AES-256-CBC
 */
fun createEncryptedAction(data: String, connection: ScaConnectionEntity): ActionAuthorization {
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

/**
 * AES-256-CBC
 */
fun createEncryptedEncryptedConsent(data: String, expiresAt: Instant, connection: ScaConnectionEntity): EncryptedConsent {
    val publicKey = KeyTools.convertPemToPublicKey(connection.rsaPublicKey, KeyTools.Algorithm.RSA)

    val key = ByteArray(32)
    SecureRandom().nextBytes(key)
    val iv = ByteArray(16)
    SecureRandom().nextBytes(iv)

    return EncryptedConsent(
        connection_id = connection.connectionId,
        expires_at = expiresAt.toString(),
        key = Base64.getEncoder().encodeToString(CryptoTools.encryptRsa(key, publicKey)),
        iv = Base64.getEncoder().encodeToString(CryptoTools.encryptRsa(iv, publicKey)),
        data = Base64.getEncoder().encodeToString(CryptoTools.encryptAes(data, key, iv))
    )
}

fun encryptAccessToken(token: String, rsaPublicKey: String): String {
    val publicKey = KeyTools.convertPemToPublicKey(rsaPublicKey, KeyTools.Algorithm.RSA)
    val jsonToken = AccessTokenWrapper(token).toJson() ?: ""
    val bytes = CryptoTools.encryptRsa(jsonToken.toByteArray(), publicKey)
//    val result = Base64.getUrlEncoder().encodeToString(bytes)
    val result = Base64.getEncoder().encodeToString(bytes)
    println("encryptAccessToken.token: $token")
    println("encryptAccessToken.jsonToken: $jsonToken")
    println("encryptAccessToken.encryptedToken: $result")
    return result
}

fun getRandomString(size: Int): String {
    val rand = Random()
    val totalCharacters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
    var randomString = ""
    for (i in 0 until size) {
        randomString += totalCharacters[rand.nextInt(totalCharacters.length - 1)]
    }
    return randomString
}
