package ru.mfp.client.impl

import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForObject
import ru.mfp.client.PaymentClientService
import ru.mfp.config.rest.PaymentIntegrationProperties
import ru.mfp.dto.AccountCreatingRequestDto
import ru.mfp.dto.PaymentCreatingRequestDto
import ru.mfp.dto.PaymentDto
import java.util.*

@Service
class PaymentClientServiceImpl(
    val paymentRestTemplate: RestTemplate,
    val properties: PaymentIntegrationProperties
) : PaymentClientService {

    override fun createAccount(accountCreatingRequestDto: AccountCreatingRequestDto): UUID =
        paymentRestTemplate.postForObject(properties.api?.createAccount ?: "", accountCreatingRequestDto, UUID::javaClass)


    override fun createPayment(paymentCreatingRequestDto: PaymentCreatingRequestDto): PaymentDto =
        paymentRestTemplate.postForObject(properties.api?.createPayment ?: "", paymentCreatingRequestDto, PaymentDto::javaClass)
}
