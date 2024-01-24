package ru.mfp.account.handler

import org.springframework.stereotype.Component
import ru.mfp.account.entity.AccountChangeReason
import ru.mfp.account.service.AccountHistoryService
import ru.mfp.common.event.EventHandler
import ru.mfp.payment.dto.DepositEventDto
import java.math.BigDecimal

@Component
class AccountDepositEventHandler(
    private val accountHistoryService: AccountHistoryService
) : EventHandler<DepositEventDto> {

    override fun getEventClass(): Class<DepositEventDto> = DepositEventDto::class.java

    override fun handle(event: DepositEventDto) {
        if (event.decision) {
            accountHistoryService.registerChange(event.accountId, BigDecimal(event.amount), AccountChangeReason.DEPOSIT)
        }
    }
}