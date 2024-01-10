package ru.mfp.dto

import java.util.*

data class AccountDto(
    val userId: UUID,
    val amount: String,
    val currency: String
)
