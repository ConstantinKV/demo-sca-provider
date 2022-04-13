/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.tools.security

import com.saltedge.provider.demo.controllers.api.sca.v1.model.CreateConnectionRequest
import com.saltedge.provider.demo.tools.JsonTools
import com.saltedge.provider.demo.tools.ResourceTools
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CryptoToolsTest {

    @Test
    fun decryptPublicRsaKeyTest() {
        //given
        val jsonString: String = ResourceTools.readKeyFile("fixtures/request_create_connection.json")
        val jsonObject: CreateConnectionRequest = JsonTools.defaultMapper.readValue(jsonString, CreateConnectionRequest::class.java)
        val encEntity = jsonObject.data.encRsaPublicKey
        val privateKeyFile = ResourceTools.readKeyFile("fixtures/argentex_app_private_rsa_dev_pkcs8.pem")

        println("encEntity:\n${encEntity.encryptedKey}")
        encEntity.encryptedKey = encEntity.encryptedKey.replace("\\n", "").replace("\n", "")
        println("updated encEntity:\n${encEntity.encryptedKey}")

        val rsaPrivateKey = KeyTools.convertPemToPrivateKey(privateKeyFile, KeyTools.Algorithm.RSA)

        //when
        val result: String = CryptoTools.decryptPublicRsaKey(encEntity, rsaPrivateKey)

        //then
        assertThat(result).startsWith("-----BEGIN PUBLIC KEY-----")
    }
}