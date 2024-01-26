package ru.mfp.user.kafka.producer.impl

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import ru.mfp.user.dto.RegistrationEventDto
import ru.mfp.user.entity.User
import ru.mfp.user.kafka.producer.AuthProducer
import java.time.LocalDateTime
import java.util.*

@Component
class AuthProducerImpl(
    private val kafkaTemplate: KafkaTemplate<UUID, String>,
    private val objectMapper: ObjectMapper
) : AuthProducer {
    @Value("\${mfp.kafka.user.topic}")
    private var topic: String = "USER"

    override fun sendRegistrationEvent(user: User) {
        kafkaTemplate.send(
            ProducerRecord(
                topic,
                UUID.randomUUID(),
                objectMapper.writeValueAsString(
                    RegistrationEventDto(
                        UUID.randomUUID(),
                        LocalDateTime.now(),
                        user.id
                    )
                )
            )
        )
    }
}