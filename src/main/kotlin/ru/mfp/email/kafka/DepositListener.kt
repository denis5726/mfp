package ru.mfp.email.kafka

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.Message
import org.springframework.stereotype.Component
import ru.mfp.common.event.EventProcessor
import ru.mfp.email.handler.DepositEventHandler

@Component
class DepositListener(
    private val processor: EventProcessor,
    private val handler: DepositEventHandler
) {

    @KafkaListener(topics = ["DEPOSIT"], groupId = "EMAIL")
    fun listenToDeposit(message: Message<String>) = processor.process(handler, message)
}
