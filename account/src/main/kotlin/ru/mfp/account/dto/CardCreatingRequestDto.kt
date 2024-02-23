package ru.mfp.account.dto

import java.util.UUID

data class CardCreatingRequestDto(
    val bankAccountId: UUID,
    val currency: String
)
