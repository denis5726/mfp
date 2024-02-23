package ru.mfp.common.dto

import java.time.LocalDateTime
import java.util.UUID

open class CommonEventDto(
    open val eventId: UUID,
    open val eventTime: LocalDateTime
)
