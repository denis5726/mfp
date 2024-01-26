package ru.mfp.payment.model

import ru.mfp.payment.dto.PaymentDto

data class PaymentCreatingResult(
    val paymentDto: PaymentDto,
    val messageCallback: () -> Unit
)
