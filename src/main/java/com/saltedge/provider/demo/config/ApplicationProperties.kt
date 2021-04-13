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
const val SCA_PROVIDER_ID = "1"
const val SCA_USER_ID = "123"
const val SCA_CONNECT_QUERY_PREFIX = "query-"
const val SCA_CONNECT_QUERY = "$SCA_CONNECT_QUERY_PREFIX$SCA_USER_ID"

/**
 * Configuration properties from application.properties
 */
@Configuration
@ConfigurationProperties
open class ApplicationProperties {
    lateinit var applicationUrl: String
    lateinit var applicationPrivateRsaKeyPem: String
    lateinit var applicationPublicRsaKeyPem: String
    lateinit var applicationPrivateDhKeyPem: String
    lateinit var applicationPublicDhKeyPem: String
    lateinit var scaServiceUrl: String
    lateinit var scaServicePublicRsaKeyPem: String

    val rsaPrivateKey: PrivateKey by lazy {
        KeyTools.convertPemToPrivateKey(
            ResourceTools.readKeyFile(applicationPrivateRsaKeyPem),
            KeyTools.Algorithm.RSA
        )!!
    }

    val rsaPublicKey: PublicKey by lazy {
        KeyTools.convertPemToPublicKey(
            ResourceTools.readKeyFile(applicationPublicRsaKeyPem),
            KeyTools.Algorithm.RSA
        )!!
    }

    val dhPrivateKey: PrivateKey by lazy {
        KeyTools.convertPemToPrivateKey(
            ResourceTools.readKeyFile(applicationPrivateDhKeyPem),
            KeyTools.Algorithm.DIFFIE_HELLMAN
        )!!
    }

    val dhPublicKey: PublicKey by lazy {
        KeyTools.convertPemToPublicKey(
            ResourceTools.readKeyFile(applicationPublicDhKeyPem),
            KeyTools.Algorithm.DIFFIE_HELLMAN
        )!!
    }

    val scaServiceRsaPublicKey: PublicKey by lazy {
        KeyTools.convertPemToPublicKey(
            ResourceTools.readKeyFile(scaServicePublicRsaKeyPem),
            KeyTools.Algorithm.RSA
        )!!
    }
}
