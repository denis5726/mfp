package ru.mfp.payment.dto

import java.math.BigDecimal
import java.util.*

data class DepositCreatingRequestDto(
    val accountId: UUID,
    val cardId: UUID,
    val amount: BigDecimal
)
