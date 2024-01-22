package ru.mfp.payment.dto

data class CurrencyExchangeRatesDto(
    val currency: String,
    val rates: List<ExchangeRate>
) {

    data class ExchangeRate(
        val currency: String,
        val rate: String
    )
}
