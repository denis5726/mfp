package ru.mfp.account.model

import java.math.BigDecimal
import java.util.*

interface AccountSnapshot {
    fun getId(): UUID
    fun getAmount(): BigDecimal
}