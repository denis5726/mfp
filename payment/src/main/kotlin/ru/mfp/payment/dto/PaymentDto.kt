package ru.mfp.payment.dto

import java.time.LocalDateTime
import java.util.UUID

data class PaymentDto(
    val paymentId: UUID,
    val operationId: UUID,
    val decision: Boolean,
    val description: String,
    val createdAt: LocalDateTime
)
