package ru.mfp.account.dto

import java.time.LocalDateTime
import java.util.UUID

data class CardDto(
    val id: UUID,
    val bankAccountId: UUID,
    val currency: String,
    val createdAt: LocalDateTime
)
