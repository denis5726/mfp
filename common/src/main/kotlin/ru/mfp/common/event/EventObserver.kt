package ru.mfp.common.event

interface EventObserver<T> {

    fun isAvailable(event: T): Boolean

    fun observer(event: T)
}