package ru.mfp.account.event.consumer

import org.springframework.stereotype.Component
import ru.mfp.account.handler.AccountDepositEventHandler
import ru.mfp.common.dto.PaymentEventDto
import ru.mfp.common.event.EventObserver

@Component
class AccountPaymentEventDtoEventObserver(
    private val handler: AccountDepositEventHandler
) : EventObserver<PaymentEventDto> {

    override fun isAvailable(event: PaymentEventDto): Boolean = true

    override fun observer(event: PaymentEventDto) = handler.handle(event)
}