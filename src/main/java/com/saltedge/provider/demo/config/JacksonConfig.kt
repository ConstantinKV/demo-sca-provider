/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.saltedge.provider.demo.tools.JsonTools
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

/**
 * Configuration of Jackson
 */
@Configuration
open class JacksonConfig {
    @Bean
    @Primary
    open fun objectMapper(): ObjectMapper? = JsonTools.defaultMapper
}