package ru.mfp.payment.dto

import java.time.LocalDateTime
import java.util.UUID

data class PaymentCreatingRequestDto(
    val id: UUID,
    val accountFrom: UUID,
    val accountTo: UUID,
    val amount: String,
    val currency: String,
    val createdAt: LocalDateTime
)
