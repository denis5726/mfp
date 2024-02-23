package ru.mfp.payment.dto

import java.math.BigDecimal
import java.util.UUID

data class WithdrawCreatingRequestDto(
    val accountId: UUID,
    val cardId: UUID,
    val amount: BigDecimal
)
