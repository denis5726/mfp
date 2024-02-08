package ru.mfp.account.service.impl

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import ru.mfp.account.entity.AccountChangeReason
import ru.mfp.account.repository.AccountHistoryRecordRepository
import ru.mfp.account.service.AccountHistoryService
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

private val log = KotlinLogging.logger { }

@Component
class ScheduledAccountUpdater(
    private val accountHistoryService: AccountHistoryService,
    private val repository: AccountHistoryRecordRepository,
) {
    @Value("\${mfp.account.time-diff}")
    private var timeDiffInSeconds = 0L
    @Value("\${mfp.account.percent-per-day}")
    private var percentPerDay = 0.0
    @Value("\${mfp.account.scheduled-updater-rate}")
    private var rateInMillis = 0L
    private val defaultScale = 30

    // Пока что обновляем все счета разом
    @Transactional
    @Scheduled(fixedRateString = "\${mfp.account.scheduled-updater-rate}")
    fun update() {
        log.info { "Updating accounts" }
        repository.getSnapshots(LocalDateTime.now().minusSeconds(timeDiffInSeconds)).forEach {
            log.info { "Updating account snapshot: AccountSnapshot(id=${it.getId()}, amount=${it.getAmount()})" }
            val amount = it.getAmount()
            val partOfDay =
                BigDecimal.valueOf(rateInMillis, defaultScale) /
                        BigDecimal.valueOf(TimeUnit.MILLISECONDS.convert(1L, TimeUnit.DAYS), defaultScale)
            val diff = amount * BigDecimal.valueOf(percentPerDay) * partOfDay / BigDecimal.valueOf(100L)
            log.info { "Diff: $diff" }
            accountHistoryService.registerChange(it.getId(), diff, AccountChangeReason.PERCENTAGE_CALCULATION)
        }
    }
}
