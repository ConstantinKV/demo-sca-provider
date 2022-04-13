/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.tools.security

import com.saltedge.provider.demo.tools.ResourceTools
import com.saltedge.provider.demo.tools.encryptAccessToken
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.*

class EncryptionToolsTest {

    @Test
    fun encryptAccessTokenTest() {
        //given
        val testToken = "85c8976c-6f6f-4671-9e1f-d2cac46d71c2"//UUID.randomUUID().toString()
        assertThat(testToken.length).isEqualTo(36)

        val publicKeyFile = ResourceTools.readKeyFile("fixtures/argentex_app_public_rsa_dev.pem")
        val privateKeyFile = ResourceTools.readKeyFile("fixtures/argentex_app_private_rsa_dev_pkcs8.pem")
        val rsaPrivateKey = KeyTools.convertPemToPrivateKey(privateKeyFile, KeyTools.Algorithm.RSA)

        //when
        val encResult: String = encryptAccessToken(testToken, publicKeyFile)

        //then
        assertThat(encResult.length).isEqualTo(344)
        val decrypt = String(CryptoTools.decryptRsa(encResult, rsaPrivateKey))
        assertThat(decrypt).isEqualTo("{\"access_token\":\"$testToken\"}")
        assertThat(encResult).isEqualTo("")
    }
}