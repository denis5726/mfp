package ru.mfp.payment.kafka.producer.impl

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import ru.mfp.common.exception.MessageSendingException
import ru.mfp.payment.dto.PaymentEventDto
import ru.mfp.payment.kafka.producer.PaymentProducer
import java.util.*

private val log = KotlinLogging.logger { }

@Component
class PaymentProducerImpl(
    private val kafkaTemplate: KafkaTemplate<UUID, String>,
    private val objectMapper: ObjectMapper
) : PaymentProducer {
    @Value("\${mfp.kafka.payment.topic}")
    private var topic: String? = null

    override fun sendPaymentEvent(event: PaymentEventDto) {
        doSendInternal(event)
    }

    private fun <T> doSendInternal(payload: T) {
        log.info { "Sending message: $payload" }
        kafkaTemplate.send(
            ProducerRecord(
                topic ?: throw MessageSendingException("Topic for depositCreatedProducer is not provided"),
                UUID.randomUUID(),
                objectMapper.writeValueAsString(payload)
            )
        )
    }
}