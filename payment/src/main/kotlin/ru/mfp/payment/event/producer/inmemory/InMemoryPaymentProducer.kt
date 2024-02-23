package ru.mfp.payment.event.producer.inmemory

import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import ru.mfp.common.dto.PaymentEventDto
import ru.mfp.common.event.EventObserver
import ru.mfp.common.event.EventProcessor
import ru.mfp.payment.event.producer.PaymentProducer

@Primary
@Component
class InMemoryPaymentProducer(
    private val observers: List<EventObserver<PaymentEventDto>>,
    private val processor: EventProcessor
) : PaymentProducer {

    override fun sendPaymentEvent(event: PaymentEventDto) = processor.processEvent(observers, event)
}