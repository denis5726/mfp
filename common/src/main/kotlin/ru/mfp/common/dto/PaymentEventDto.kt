package ru.mfp.common.dto

import java.time.LocalDateTime
import java.util.UUID

data class PaymentEventDto(
    val userId: UUID,
    val paymentId: UUID,
    val operationId: UUID?,
    val accountId: UUID,
    val cardId: UUID,
    val decision: Boolean,
    val description: String?,
    val amount: String,
    val currency: String,
    val type: PaymentType,
    override val eventId: UUID = UUID.randomUUID(),
    override val eventTime: LocalDateTime = LocalDateTime.now()
) : CommonEventDto(eventId, eventTime) {

    enum class PaymentType {
        DEPOSIT, WITHDRAW
    }
}
