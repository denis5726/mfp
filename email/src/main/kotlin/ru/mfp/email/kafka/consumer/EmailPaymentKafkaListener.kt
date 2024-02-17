package ru.mfp.email.kafka.consumer

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.Message
import org.springframework.stereotype.Component
import ru.mfp.common.event.EventProcessor
import ru.mfp.email.handler.EmailDepositEventHandler

@Component
class EmailPaymentKafkaListener(
    private val processor: EventProcessor,
    private val handler: EmailDepositEventHandler
) {

    @KafkaListener(
        topics = ["\${mfp.kafka.payment.topic}"],
        groupId = "\${mfp.kafka.email.consumer-group}",
        autoStartup = "true"
    )
    fun listenToPayment(message: Message<String>) = processor.process(handler, message)
}