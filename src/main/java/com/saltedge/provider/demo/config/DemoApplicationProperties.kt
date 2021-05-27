/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.config

import com.saltedge.provider.demo.tools.ResourceTools
import com.saltedge.provider.demo.tools.security.KeyTools
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import java.security.PrivateKey
import java.security.PublicKey

const val APP_LINK_PREFIX_CONNECT = "authenticator://saltedge.com/connect?configuration="
const val SCA_USER_ID = "123"
const val SCA_USER_NAME = "username"
const val SCA_USER_PASSWORD = "secret"
const val SCA_CONNECT_QUERY_PREFIX = "query-"
const val SCA_CONNECT_QUERY = "$SCA_CONNECT_QUERY_PREFIX$SCA_USER_ID"

/**
 * Configuration properties from application.properties
 */
@Configuration
@ConfigurationProperties
open class DemoApplicationProperties {
    lateinit var applicationUrl: String
    lateinit var applicationPrivateRsaKeyFile: String
    lateinit var applicationPublicRsaKeyFile: String
    lateinit var scaServiceUrl: String
    var scaServicePublicRsaKeyFile: String = ""
    lateinit var scaProviderId: String

    private var _scaServiceRsaPublicKeyPem: String = ""

    val rsaPrivateKey: PrivateKey by lazy {
        KeyTools.convertPemToPrivateKey(
            ResourceTools.readKeyFile(applicationPrivateRsaKeyFile),
            KeyTools.Algorithm.RSA
        )!!
    }

    val rsaPublicKey: PublicKey by lazy {
        KeyTools.convertPemToPublicKey(
            ResourceTools.readKeyFile(applicationPublicRsaKeyFile),
            KeyTools.Algorithm.RSA
        )!!
    }

    var scaServiceRsaPublicKeyPem: String
        get() = getScaRsaPem()
        set(value) = setScaRsaPem(value)

    val scaServiceRsaPublicKey: PublicKey
        get() = KeyTools.convertPemToPublicKey(scaServiceRsaPublicKeyPem, KeyTools.Algorithm.RSA)!!

    private fun getScaRsaPem(): String {
        if (_scaServiceRsaPublicKeyPem.isEmpty()) {
            _scaServiceRsaPublicKeyPem = ResourceTools.readKeyFile(scaServicePublicRsaKeyFile)
        }
        return _scaServiceRsaPublicKeyPem
    }

    private fun setScaRsaPem(pem: String) {
        KeyTools.convertPemToPublicKey(pem, KeyTools.Algorithm.RSA)?.let {
            _scaServiceRsaPublicKeyPem = pem
        }
    }
}
