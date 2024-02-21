package ru.mfp.payment.producer.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import java.util.*
import mu.KotlinLogging
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import ru.mfp.common.dto.PaymentEventDto
import ru.mfp.payment.producer.PaymentProducer

private val log = KotlinLogging.logger { }

@Component
class PaymentProducerImpl(
    private val kafkaTemplate: KafkaTemplate<UUID, String>,
    private val objectMapper: ObjectMapper
) : PaymentProducer {
    @Value("\${mfp.kafka.payment.topic}")
    private var topic: String = "PAYMENT"

    override fun sendPaymentEvent(event: PaymentEventDto) {
        doSendInternal(event)
    }

    private fun <T> doSendInternal(payload: T) {
        log.info { "Sending message: $payload" }
        kafkaTemplate.send(
            ProducerRecord(
                topic,
                UUID.randomUUID(),
                objectMapper.writeValueAsString(payload)
            )
        )
    }
}