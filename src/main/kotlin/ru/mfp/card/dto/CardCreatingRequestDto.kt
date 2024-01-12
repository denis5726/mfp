package ru.mfp.card.dto

import java.util.*

data class CardCreatingRequestDto(
    val bankAccountId: UUID,
    val currency: String
)
