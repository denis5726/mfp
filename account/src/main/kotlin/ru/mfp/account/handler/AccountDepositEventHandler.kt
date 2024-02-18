package ru.mfp.account.handler

import org.springframework.stereotype.Component
import ru.mfp.account.entity.AccountChangeReason
import ru.mfp.account.service.AccountHistoryService
import ru.mfp.common.event.EventHandler
import ru.mfp.common.dto.PaymentEventDto
import java.math.BigDecimal

@Component
class AccountDepositEventHandler(
    private val accountHistoryService: AccountHistoryService
) : EventHandler<PaymentEventDto> {

    override fun getEventClass(): Class<PaymentEventDto> = PaymentEventDto::class.java

    override fun handle(event: PaymentEventDto) {
        if (event.decision) {
            val amount = BigDecimal(event.amount)
            accountHistoryService.registerChange(
                event.accountId,
                if (event.type == PaymentEventDto.PaymentType.DEPOSIT) amount else -amount,
                if (event.type == PaymentEventDto.PaymentType.DEPOSIT)
                    AccountChangeReason.DEPOSIT else AccountChangeReason.WITHDRAW
            )
        }
    }
}