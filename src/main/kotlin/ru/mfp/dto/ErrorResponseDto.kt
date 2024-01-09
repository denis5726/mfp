package ru.mfp.dto

import org.springframework.http.HttpStatus
import java.time.LocalDateTime

data class ErrorResponseDto(
    val status: HttpStatus,
    val message: String,
    val time: LocalDateTime,
    val path: String
)
