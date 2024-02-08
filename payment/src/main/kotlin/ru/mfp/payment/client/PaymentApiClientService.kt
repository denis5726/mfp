package ru.mfp.payment.client

import ru.mfp.account.dto.AccountCreatingRequestDto
import ru.mfp.payment.dto.CurrencyExchangeRatesDto
import ru.mfp.payment.dto.PaymentCreatingRequestDto
import ru.mfp.payment.dto.PaymentDto
import java.util.*

interface PaymentApiClientService {

    fun createAccount(accountCreatingRequestDto: AccountCreatingRequestDto): UUID

    fun createPayment(paymentCreatingRequestDto: PaymentCreatingRequestDto): PaymentDto

    fun findCurrencyExchangeRates(currency: Currency): CurrencyExchangeRatesDto
}
