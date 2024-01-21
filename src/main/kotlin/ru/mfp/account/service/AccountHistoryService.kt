package ru.mfp.account.service

import ru.mfp.account.entity.AccountChangeReason
import java.math.BigDecimal
import java.util.*

fun interface AccountHistoryService {

    fun registerChange(accountId: UUID, diff: BigDecimal, reason: AccountChangeReason)
}
