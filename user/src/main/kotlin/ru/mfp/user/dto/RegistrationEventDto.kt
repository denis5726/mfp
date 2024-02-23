package ru.mfp.user.dto

import java.time.LocalDateTime
import java.util.UUID
import ru.mfp.common.dto.CommonEventDto

data class RegistrationEventDto(
    override val eventId: UUID,
    override val eventTime: LocalDateTime,
    val userId: UUID
) : CommonEventDto(eventId, eventTime)
