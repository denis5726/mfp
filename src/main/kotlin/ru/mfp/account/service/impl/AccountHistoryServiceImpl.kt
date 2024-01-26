package ru.mfp.account.service.impl

import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.mfp.account.entity.AccountChangeReason
import ru.mfp.account.entity.AccountHistoryRecord
import ru.mfp.account.repository.AccountHistoryRecordRepository
import ru.mfp.account.repository.AccountRepository
import ru.mfp.account.service.AccountHistoryService
import ru.mfp.common.exception.IllegalServerStateException
import java.math.BigDecimal
import java.util.*

private val log = KotlinLogging.logger { }

@Service
class AccountHistoryServiceImpl(
    val repository: AccountHistoryRecordRepository,
    val accountRepository: AccountRepository
) : AccountHistoryService {

    @Transactional
    override fun registerChange(accountId: UUID, diff: BigDecimal, reason: AccountChangeReason) {
        val record = AccountHistoryRecord()
        val account =
            accountRepository.findById(accountId).orElseThrow { IllegalServerStateException("Account not found") }
        record.account = account
        record.diff = diff
        record.changeReason = reason
        log.info { "Saving account change: $record" }
        repository.save(record)
    }
}