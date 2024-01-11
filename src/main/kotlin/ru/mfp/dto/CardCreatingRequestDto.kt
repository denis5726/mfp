package ru.mfp.dto

import java.util.*

data class CardCreatingRequestDto(
    val bankAccountId: UUID,
    val currency: String
)
