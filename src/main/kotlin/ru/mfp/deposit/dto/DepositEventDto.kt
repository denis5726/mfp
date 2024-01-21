package ru.mfp.deposit.dto

import ru.mfp.common.dto.CommonEventDto
import java.time.LocalDateTime
import java.util.*

data class DepositEventDto(
    override val eventId: UUID,
    override val eventTime: LocalDateTime,
    val userId: UUID,
    val paymentId: UUID,
    val operationId: UUID?,
    val accountId: UUID,
    val cardId: UUID,
    val decision: Boolean,
    val description: String?
) : CommonEventDto(eventId, eventTime)
