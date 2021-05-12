/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.config

import com.saltedge.provider.demo.controllers.api.sca.v1.BaseController
import com.saltedge.provider.demo.controllers.api.sca.v1.SignatureInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * Configuration of interceptors
 */
@Configuration
open class DemoApplicationInterceptorsConfig : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(SignatureInterceptor()).addPathPatterns("${BaseController.API_BASE_PATH}/*")
    }
}