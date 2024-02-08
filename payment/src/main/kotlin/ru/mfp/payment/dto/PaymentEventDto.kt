package ru.mfp.payment.dto

import ru.mfp.common.dto.CommonEventDto
import java.time.LocalDateTime
import java.util.*

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
