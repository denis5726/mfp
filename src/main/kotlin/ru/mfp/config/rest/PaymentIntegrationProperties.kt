package ru.mfp.config.rest

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("mfp.payment")
data class PaymentIntegrationProperties(
    var url: String?,
    var api: PaymentIntegrationApiProperties?
){

    data class PaymentIntegrationApiProperties(
        var createAccount: String?,
        var createPayment: String?
    )
}
