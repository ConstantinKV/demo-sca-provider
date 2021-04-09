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

    val privateRsaKey: PrivateKey by lazy {
        KeyTools.convertPemToPrivateKey(
            ResourceTools.readKeyFile(applicationPrivateRsaKeyPem),
            KeyTools.Algorithm.RSA
        )!!
    }

    val publicRsaKey: PublicKey by lazy {
        KeyTools.convertPemToPublicKey(
            ResourceTools.readKeyFile(applicationPublicRsaKeyPem),
            KeyTools.Algorithm.RSA
        )!!
    }

    val privateDhKey: PrivateKey by lazy {
        KeyTools.convertPemToPrivateKey(
            ResourceTools.readKeyFile(applicationPrivateDhKeyPem),
            KeyTools.Algorithm.DIFFIE_HELLMAN
        )!!
    }

    val publicDhKey: PublicKey by lazy {
        KeyTools.convertPemToPublicKey(
            ResourceTools.readKeyFile(applicationPublicDhKeyPem),
            KeyTools.Algorithm.DIFFIE_HELLMAN
        )!!
    }

    val scaServicePublicRsaKey: PublicKey by lazy {
        KeyTools.convertPemToPublicKey(
            ResourceTools.readKeyFile(scaServicePublicRsaKeyPem),
            KeyTools.Algorithm.RSA
        )!!
    }
}
