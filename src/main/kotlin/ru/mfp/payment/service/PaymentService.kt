package ru.mfp.payment.service

import ru.mfp.payment.model.PaymentCreatingResult
import java.math.BigDecimal
import java.util.*

fun interface PaymentService {

    fun doPayment(
        userId: UUID,
        accountId: UUID,
        cardId: UUID,
        amount: BigDecimal,
        toBank: Boolean
    ): PaymentCreatingResult
}