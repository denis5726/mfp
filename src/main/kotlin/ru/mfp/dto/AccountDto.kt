package ru.mfp.dto

import java.util.*

data class AccountDto(
    val user: UUID,
    val amount: String,
    val currency: String
)
