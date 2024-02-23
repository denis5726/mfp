package ru.mfp.account.service

import java.math.BigDecimal
import java.util.UUID
import ru.mfp.account.entity.AccountChangeReason

fun interface AccountHistoryService {

    fun registerChange(accountId: UUID, diff: BigDecimal, reason: AccountChangeReason)
}
