/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.tools

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule

/**
 * Set of tools for JSON objects creation
 */
object JsonTools {

    val defaultMapper = createDefaultMapper()
    /**
     * Creates mapper with deserialization option to fail on unknown property
     *
     * @return Jackson's ObjectMapper
     */
    fun createDefaultMapper(): ObjectMapper {
        val objectMapper = ObjectMapper()
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        objectMapper.registerModule(JavaTimeModule())
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        return objectMapper
    }
}

fun Any.toJson(): String? {
    return JsonTools.defaultMapper.writeValueAsString(this);
}
