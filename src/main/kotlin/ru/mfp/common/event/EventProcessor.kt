package ru.mfp.common.event

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.springframework.messaging.Message
import org.springframework.stereotype.Component
import ru.mfp.common.dto.CommonEventDto
import ru.mfp.common.exception.EventParsingException

private val log = KotlinLogging.logger {  }

@Component
class EventProcessor(
    private val objectMapper: ObjectMapper
) {

    fun <T : CommonEventDto> process(handler: EventHandler<T>, message: Message<String>) {
        val clazz = handler.getEventClass()
        log.debug { "Parsing kafka message payload: ${message.payload}, class: $clazz" }
        val event = parseEvent(message.payload, clazz)
        log.debug { "Handling event: $event" }
        try {
            handler.handle(event)
        } catch (e: Exception) {
            log.error { "Exception while event handling: ${e.message}" }
            throw e
        }
    }

    private fun <T: CommonEventDto> parseEvent(payload: String, clazz: Class<T>): T {
        try {
            return objectMapper.readValue(payload, clazz)
        } catch (e: JsonProcessingException) {
            log.error { "Event parsing exception: ${e.message}" }
            throw EventParsingException("Exception while parsing event: ${e.message}")
        }
    }
}
