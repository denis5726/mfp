package ru.mfp.stub.dto

import java.time.LocalDateTime
import java.util.*

data class PaymentDto(
    val paymentId: UUID,
    val operationId: UUID,
    val decision: Boolean,
    val description: String,
    val createdAt: LocalDateTime
)
