package ru.mfp.payment.client

import java.util.Currency
import java.util.UUID
import ru.mfp.account.dto.AccountCreatingRequestDto
import ru.mfp.payment.dto.CurrencyExchangeRatesDto
import ru.mfp.payment.dto.PaymentCreatingRequestDto
import ru.mfp.payment.dto.PaymentDto

interface PaymentApiClientService {

    fun createAccount(accountCreatingRequestDto: AccountCreatingRequestDto): UUID

    fun createPayment(paymentCreatingRequestDto: PaymentCreatingRequestDto): PaymentDto

    fun findCurrencyExchangeRates(currency: Currency): CurrencyExchangeRatesDto
}
