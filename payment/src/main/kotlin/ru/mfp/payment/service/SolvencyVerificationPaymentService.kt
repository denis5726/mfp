package ru.mfp.payment.service

import java.util.UUID
import ru.mfp.common.model.JwtAuthentication

fun interface SolvencyVerificationPaymentService {

    fun doVerificationPayment(cardId: UUID, authentication: JwtAuthentication)
}