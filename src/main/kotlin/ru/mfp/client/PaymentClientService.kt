package ru.mfp.client

import ru.mfp.dto.AccountCreatingRequestDto
import ru.mfp.dto.PaymentCreatingRequestDto
import ru.mfp.dto.PaymentDto
import java.util.*

interface PaymentClientService {

    fun createAccount(accountCreatingRequestDto: AccountCreatingRequestDto): UUID

    fun createPayment(paymentCreatingRequestDto: PaymentCreatingRequestDto): PaymentDto
}
