package ru.mfp.auth.dto

data class SignInDto(
    val email: String,
    val passwordHash: String
)
