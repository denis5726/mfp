package ru.mfp.client.impl

import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForObject
import ru.mfp.client.PaymentClientService
import ru.mfp.config.rest.PaymentIntegrationProperties
import ru.mfp.dto.CreatedAccountDto
import ru.mfp.dto.CreatedPaymentDto
import ru.mfp.dto.PaymentDto
import java.util.*

@Service
class PaymentClientServiceImpl(
    val paymentRestTemplate: RestTemplate,
    val properties: PaymentIntegrationProperties
) : PaymentClientService {

    override fun createAccount(createdAccountDto: CreatedAccountDto): UUID =
        paymentRestTemplate.postForObject(properties.api?.createAccount ?: "", createdAccountDto, UUID::javaClass)


    override fun createPayment(createdPaymentDto: CreatedPaymentDto): PaymentDto =
        paymentRestTemplate.postForObject(properties.api?.createPayment ?: "", createdPaymentDto, PaymentDto::javaClass)
}