package ru.mfp.account.model

import java.math.BigDecimal
import java.util.UUID

interface AccountSnapshot {
    fun getId(): UUID
    fun getAmount(): BigDecimal
}