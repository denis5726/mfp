package ru.mfp.payment.dto

import java.time.LocalDateTime
import java.util.UUID

data class DepositDto(
    val paymentId: UUID,
    val operationId: UUID?,
    val accountId: UUID,
    val cardId: UUID,
    val amount: String,
    val createdAt: LocalDateTime
)
