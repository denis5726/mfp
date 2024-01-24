package ru.mfp.payment.dto

import java.time.LocalDateTime
import java.util.*

data class DepositDto(
    val paymentId: UUID,
    val operationId: UUID?,
    val accountId: UUID,
    val cardId: UUID,
    val amount: String,
    val currency: String,
    val createdAt: LocalDateTime
)
