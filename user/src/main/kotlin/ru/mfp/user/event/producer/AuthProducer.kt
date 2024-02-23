package ru.mfp.user.event.producer

import ru.mfp.user.entity.User

fun interface AuthProducer {

    // TODO Убрать ответственность формирования события с продюсера
    fun sendRegistrationEvent(user: User)
}