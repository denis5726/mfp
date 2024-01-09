package ru.mfp.dto

data class SignInDto(
    val login: String?,
    val passwordHash: String?
)
