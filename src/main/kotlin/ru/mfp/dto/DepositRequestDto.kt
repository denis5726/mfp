package ru.mfp.dto

import java.math.BigDecimal
import java.util.*

data class DepositRequestDto(
    val accountId: UUID?,
    val cardId: UUID?,
    val amount: BigDecimal?
)
