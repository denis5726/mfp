package ru.mfp.user.event.producer.inmemory

import java.time.LocalDateTime
import java.util.UUID
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import ru.mfp.common.event.EventObserver
import ru.mfp.common.event.EventProcessor
import ru.mfp.user.dto.RegistrationEventDto
import ru.mfp.user.entity.User
import ru.mfp.user.event.producer.AuthProducer

@Primary
@Component
class InMemoryAuthProducer(
    private val observers: List<EventObserver<RegistrationEventDto>>,
    private val processor: EventProcessor
) : AuthProducer {

    override fun sendRegistrationEvent(user: User) = processor.processEvent(
        observers, RegistrationEventDto(
            UUID.randomUUID(),
            LocalDateTime.now(),
            user.id
        )
    )
}