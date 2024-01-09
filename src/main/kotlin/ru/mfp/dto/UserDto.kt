package ru.mfp.dto

import java.util.UUID

data class UserDto(
    val id: UUID,
    val login: String
)
