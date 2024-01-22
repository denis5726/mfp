package ru.mfp.payment.impl

import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import org.springframework.web.client.postForObject
import ru.mfp.account.dto.AccountCreatingRequestDto
import ru.mfp.common.config.rest.PaymentIntegrationProperties
import ru.mfp.deposit.exception.PaymentApiException
import ru.mfp.payment.PaymentApiClientService
import ru.mfp.payment.dto.CurrencyExchangeRatesDto
import ru.mfp.payment.dto.PaymentCreatingRequestDto
import ru.mfp.payment.dto.PaymentDto
import java.util.*

@Service
class PaymentApiClientServiceImpl(
    val paymentRestTemplate: RestTemplate,
    val properties: PaymentIntegrationProperties
) : PaymentApiClientService {

    override fun createAccount(accountCreatingRequestDto: AccountCreatingRequestDto): UUID =
        try {
            paymentRestTemplate.postForObject(
                properties.api?.createAccount ?: "",
                accountCreatingRequestDto,
                UUID::javaClass
            )
        } catch (e: Exception) {
            handleException(e)
        }

    override fun createPayment(paymentCreatingRequestDto: PaymentCreatingRequestDto): PaymentDto =
        try {
            paymentRestTemplate.postForObject(
                properties.api?.createPayment ?: "",
                paymentCreatingRequestDto,
                PaymentDto::javaClass
            )
        } catch (e: Exception) {
            handleException(e)
        }

    override fun findCurrencyExchangeRates(currency: Currency): CurrencyExchangeRatesDto =
        try {
            paymentRestTemplate.getForObject<CurrencyExchangeRatesDto>(
                properties.api?.findCurrencyExchangeRates ?: "",
                currency.currencyCode
            )
        } catch (e: Exception) {
            handleException(e)
        }

    private fun handleException(e: Exception): Nothing {
        throw PaymentApiException(e.message ?: "Empty message")
    }
}
