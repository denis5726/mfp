package ru.mfp.dto

data class SignInDto(
    val email: String,
    val passwordHash: String
)
