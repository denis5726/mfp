package ru.mfp.payment.service

import java.math.BigDecimal
import java.util.UUID
import ru.mfp.common.model.JwtAuthentication
import ru.mfp.payment.model.PaymentCreatingResult

fun interface PaymentService {

    fun doPayment(
        authentication: JwtAuthentication,
        accountId: UUID,
        cardId: UUID,
        amount: BigDecimal,
        toBank: Boolean
    ): PaymentCreatingResult
}