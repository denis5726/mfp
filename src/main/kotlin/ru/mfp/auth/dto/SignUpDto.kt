package ru.mfp.auth.dto

import jakarta.validation.constraints.Email

data class SignUpDto(
    @field:Email(message = "Invalid email format!")
    val email: String,
    val passwordHash: String
)
