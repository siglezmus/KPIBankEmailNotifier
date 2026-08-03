package com.kpibank.email_notifier.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import tools.jackson.databind.ObjectMapper
import tools.jackson.module.kotlin.jacksonObjectMapper

@Configuration
class JacksonConfig {

    @Bean
    fun objectMapper(): ObjectMapper {
        return jacksonObjectMapper()
    }
}