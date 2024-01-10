package ru.mfp.dto

import java.time.LocalDateTime
import java.util.*

data class CardDto(
    val id: UUID,
    val accountId: UUID,
    val createdAt: LocalDateTime
)
