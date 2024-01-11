package ru.mfp.dto

import java.util.*

data class CreatedCardDto(
    val bankAccountId: UUID,
    val currency: String
)
