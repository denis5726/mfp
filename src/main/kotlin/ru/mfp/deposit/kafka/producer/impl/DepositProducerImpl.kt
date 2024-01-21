package ru.mfp.deposit.kafka.producer.impl

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import ru.mfp.common.exception.MessageSendingException
import ru.mfp.deposit.dto.DepositCreatingRequestDto
import ru.mfp.deposit.dto.DepositEventDto
import ru.mfp.deposit.entity.Deposit
import ru.mfp.deposit.kafka.producer.DepositProducer
import java.time.LocalDateTime
import java.util.*

private val log = KotlinLogging.logger { }

@Component
class DepositProducerImpl(
    val kafkaTemplate: KafkaTemplate<UUID, String>,
    val objectMapper: ObjectMapper
) : DepositProducer {
    @Value("\${mfp.kafka.deposit.topic}")
    private var topic: String? = null

    override fun sendDepositCreatedEvent(deposit: Deposit) {
        doSendInternal(createDepositCreatedMessagePayload(deposit))
    }

    override fun sendDepositErrorEvent(
        request: DepositCreatingRequestDto,
        userId: UUID,
        message: String?,
        paymentId: UUID,
        currency: Currency
    ) {
        doSendInternal(createDepositErrorMessagePayload(request, userId, message, paymentId, currency))
    }

    private fun createDepositCreatedMessagePayload(deposit: Deposit) =
        DepositEventDto(
            UUID.randomUUID(),
            LocalDateTime.now(),
            deposit.account.user.id,
            deposit.paymentId,
            deposit.operationId,
            deposit.account.id,
            deposit.card.id,
            true,
            "Deposit created",
            deposit.amount.toString(),
            deposit.account.currency.currencyCode
        )

    private fun createDepositErrorMessagePayload(
        request: DepositCreatingRequestDto,
        userId: UUID,
        message: String?,
        paymentId: UUID,
        currency: Currency
    ) =
        DepositEventDto(
            UUID.randomUUID(),
            LocalDateTime.now(),
            userId,
            paymentId,
            null,
            request.accountId,
            request.cardId,
            false,
            message,
            request.amount.toString(),
            currency.currencyCode
        )

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