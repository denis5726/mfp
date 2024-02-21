package ru.mfp.common.event.impl

import mu.KotlinLogging
import org.springframework.stereotype.Component
import ru.mfp.common.event.EventObserver
import ru.mfp.common.event.EventProcessor

private val log = KotlinLogging.logger { }

@Component
class EventProcessorImpl : EventProcessor {

    override fun <T> processEvent(handlers: List<EventObserver<T>>, event: T) {
        handlers.forEach {
            try {
                if (it.isAvailable(event)) {
                    it.observer(event)
                }
            } catch (e: RuntimeException) {
                log.error { "Exception in event observer: ${e.message}" }
            }
        }
    }
}