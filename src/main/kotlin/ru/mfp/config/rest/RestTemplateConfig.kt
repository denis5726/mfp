package ru.mfp.config.rest

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class RestTemplateConfig {

    @Bean
    @ConfigurationProperties
    fun paymentRestTemplate(paymentIntegrationProperties: PaymentIntegrationProperties): RestTemplate {
        return RestTemplateBuilder()
            .rootUri(paymentIntegrationProperties.url)
            .build()
    }
}