package ru.mfp.payment

import ru.mfp.account.dto.AccountCreatingRequestDto
import ru.mfp.payment.dto.PaymentCreatingRequestDto
import ru.mfp.payment.dto.PaymentDto
import java.util.*

interface PaymentClientService {

    fun createAccount(accountCreatingRequestDto: AccountCreatingRequestDto): UUID

    fun createPayment(paymentCreatingRequestDto: PaymentCreatingRequestDto): PaymentDto
}
