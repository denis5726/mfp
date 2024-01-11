package ru.mfp.dto

import java.util.*

data class AccountDto(
    val id: UUID,
    val amount: String,
    val currency: String
)
