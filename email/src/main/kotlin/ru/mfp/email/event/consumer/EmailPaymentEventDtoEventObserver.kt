package ru.mfp.email.event.consumer

import org.springframework.stereotype.Component
import ru.mfp.common.dto.PaymentEventDto
import ru.mfp.common.event.EventObserver
import ru.mfp.email.handler.EmailDepositEventHandler

@Component
class EmailPaymentEventDtoEventObserver(
    private val handler: EmailDepositEventHandler
) : EventObserver<PaymentEventDto> {

    override fun isAvailable(event: PaymentEventDto): Boolean = true

    override fun observer(event: PaymentEventDto) = handler.handle(event)
}