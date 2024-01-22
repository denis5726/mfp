package ru.mfp.auth.dto

import java.util.*

data class UserDto(
    val id: UUID,
    val email: String,
    val status: String
)
