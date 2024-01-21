package ru.mfp.payment.impl

import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForObject
import ru.mfp.account.dto.AccountCreatingRequestDto
import ru.mfp.common.config.rest.PaymentIntegrationProperties
import ru.mfp.deposit.exception.PaymentApiException
import ru.mfp.payment.PaymentClientService
import ru.mfp.payment.dto.PaymentCreatingRequestDto
import ru.mfp.payment.dto.PaymentDto
import java.util.*

@Service
class PaymentClientServiceImpl(
    val paymentRestTemplate: RestTemplate,
    val properties: PaymentIntegrationProperties
) : PaymentClientService {

    override fun createAccount(accountCreatingRequestDto: AccountCreatingRequestDto): UUID =
        try {
            paymentRestTemplate.postForObject(
                properties.api?.createAccount ?: "",
                accountCreatingRequestDto,
                UUID::javaClass
            )
        } catch (e: Exception) {
            throw PaymentApiException(e.message ?: "Empty message")
        }

    override fun createPayment(paymentCreatingRequestDto: PaymentCreatingRequestDto): PaymentDto =
        try {
            paymentRestTemplate.postForObject(
                properties.api?.createPayment ?: "",
                paymentCreatingRequestDto,
                PaymentDto::javaClass
            )
        } catch (e: Exception) {
            throw PaymentApiException(e.message ?: "Empty message")
        }

}
