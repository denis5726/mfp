package ru.mfp.auth.dto

data class SignUpDto(
    val email: String,
    val passwordHash: String
)
