package ru.mfp.common.config.rest

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("mfp.payment-service")
data class PaymentIntegrationProperties(
    var url: String?,
    var api: PaymentIntegrationApiProperties?
) {

    data class PaymentIntegrationApiProperties(
        var createAccount: String?,
        var createPayment: String?,
        var findCurrencyExchangeRates: String?
    )
}
