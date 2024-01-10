package ru.mfp.client

import ru.mfp.dto.CreatedAccountDto
import ru.mfp.dto.CreatedPaymentDto
import ru.mfp.dto.PaymentDto
import java.util.UUID

interface PaymentClientService {

    fun createAccount(createdAccountDto: CreatedAccountDto): UUID

    fun createPayment(createdPaymentDto: CreatedPaymentDto): PaymentDto
}