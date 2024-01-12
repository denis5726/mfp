package ru.mfp.deposit.dto

import java.time.LocalDateTime
import java.util.*

data class DepositDto(
    val paymentId: UUID,
    val operationId: UUID,
    val accountId: UUID,
    val cardId: UUID,
    val decision: Boolean,
    val description: String,
    val createdAt: LocalDateTime
)
