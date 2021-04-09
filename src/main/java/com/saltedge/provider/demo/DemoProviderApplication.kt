/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * Example Application which simulates work of ASPSP/Bank application.
 * This application is just a POC (Proof Of Concept).
 */
@SpringBootApplication
open class DemoProviderApplication

fun main(args: Array<String>) { runApplication<DemoProviderApplication>(*args) }
