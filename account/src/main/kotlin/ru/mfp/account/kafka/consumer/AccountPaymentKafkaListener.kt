package ru.mfp.account.kafka.consumer

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.Message
import org.springframework.stereotype.Component
import ru.mfp.account.handler.AccountDepositEventHandler
import ru.mfp.common.event.KafkaEventProcessor

@Component
class AccountPaymentKafkaListener(
    private val processor: KafkaEventProcessor,
    private val handler: AccountDepositEventHandler
) {

    @KafkaListener(
        topics = ["\${mfp.kafka.payment.topic}"],
        groupId = "\${mfp.kafka.account.consumer-group}",
        autoStartup = "true"
    )
    fun listenToPayment(message: Message<String>) = processor.process(handler, message)
}