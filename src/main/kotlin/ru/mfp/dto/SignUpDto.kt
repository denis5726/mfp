package ru.mfp.dto

data class SignUpDto(
    val email: String,
    val passwordHash: String
)
