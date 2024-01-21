package ru.mfp.account.kafka.consumer

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.Message
import org.springframework.stereotype.Component
import ru.mfp.account.handler.AccountDepositEventHandler
import ru.mfp.common.event.EventProcessor

@Component
class AccountDepositKafkaListener(
    private val processor: EventProcessor,
    private val handler: AccountDepositEventHandler
) {

    @KafkaListener(
        topics = ["\${mfp.kafka.deposit.topic}"],
        groupId = "\${mfp.kafka.account.consumer-group}",
        autoStartup = "true"
    )
    fun listenToDeposit(message: Message<String>) = processor.process(handler, message)
}