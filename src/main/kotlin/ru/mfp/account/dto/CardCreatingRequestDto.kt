package ru.mfp.account.dto

import java.util.*

data class CardCreatingRequestDto(
    val bankAccountId: UUID,
    val currency: String
)
