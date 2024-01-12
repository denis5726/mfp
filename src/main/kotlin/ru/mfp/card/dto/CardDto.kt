package ru.mfp.card.dto

import java.time.LocalDateTime
import java.util.*

data class CardDto(
    val id: UUID,
    val bankAccountId: UUID,
    val currency: String,
    val createdAt: LocalDateTime
)
