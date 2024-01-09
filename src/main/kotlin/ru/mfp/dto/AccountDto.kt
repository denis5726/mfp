package ru.mfp.dto

import java.util.UUID

data class AccountDto(
    val id: UUID,
    val login: String
)
