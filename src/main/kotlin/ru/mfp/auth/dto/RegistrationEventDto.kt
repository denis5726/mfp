package ru.mfp.auth.dto

import ru.mfp.common.dto.CommonEventDto
import java.time.LocalDateTime
import java.util.*

data class RegistrationEventDto(
    override val eventId: UUID,
    override val eventTime: LocalDateTime,
    val userId: UUID
) : CommonEventDto(eventId, eventTime)
