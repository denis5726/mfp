package ru.mfp.account.dto

import java.util.UUID

data class AccountDto(
    val id: UUID,
    val amount: String,
    val currency: String
)
