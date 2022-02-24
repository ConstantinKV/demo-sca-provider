/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.config

import com.saltedge.provider.demo.tools.JsonTools
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate
import java.time.Duration

/**
 * Configuration of RestTemplate
 */
@Configuration
open class RestTemplateConfiguration {
    @Bean
    @Primary
    open fun restTemplate(builder: RestTemplateBuilder): RestTemplate {
        return builder
            .setConnectTimeout(Duration.ofMillis(20_000))
            .setReadTimeout(Duration.ofMillis(20_000))
            .messageConverters(mappingJacksonHttpMessageConverter())
            .build()
    }

    open fun mappingJacksonHttpMessageConverter(): MappingJackson2HttpMessageConverter {
        val messageConverter = MappingJackson2HttpMessageConverter()
        messageConverter.setPrettyPrint(false)
        messageConverter.objectMapper = JsonTools.createDefaultMapper()
        return messageConverter
    }
}