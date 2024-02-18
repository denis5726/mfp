package ru.mfp.account.service.impl

import java.math.BigDecimal
import java.util.UUID
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.mfp.account.entity.AccountChangeReason
import ru.mfp.account.entity.AccountHistoryRecord
import ru.mfp.account.repository.AccountHistoryRecordRepository
import ru.mfp.account.repository.AccountRepository
import ru.mfp.account.service.AccountHistoryService
import ru.mfp.common.exception.IllegalServerStateException

private val log = KotlinLogging.logger { }

@Service
class AccountHistoryServiceImpl(
    private val repository: AccountHistoryRecordRepository,
    private val accountRepository: AccountRepository
) : AccountHistoryService {

    @Transactional
    override fun registerChange(accountId: UUID, diff: BigDecimal, reason: AccountChangeReason) {
        val account =
            accountRepository.findById(accountId).orElseThrow { IllegalServerStateException("Account not found") }
        val record = AccountHistoryRecord(
            account = account,
            diff = diff,
            changeReason = reason
        )
        repository.saveAndFlush(record)
        account.amount += diff
        accountRepository.saveAndFlush(account)
        log.info { "Saved account change: $record" }
    }
}
