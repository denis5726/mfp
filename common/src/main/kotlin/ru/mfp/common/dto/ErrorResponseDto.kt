package ru.mfp.common.dto

import java.time.LocalDateTime
import org.springframework.http.HttpStatus

data class ErrorResponseDto(
    val status: HttpStatus,
    val message: String,
    val time: LocalDateTime,
    val path: String
)
