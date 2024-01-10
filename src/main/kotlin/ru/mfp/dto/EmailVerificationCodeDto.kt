package ru.mfp.dto

import java.time.LocalDateTime

data class EmailVerificationCodeDto(
    val code: String,
    val expiredAt: LocalDateTime
)
