package ru.mfp.common.event

interface EventProcessor {

    fun <T> processEvent(handlers: List<EventObserver<T>>, event: T)
}