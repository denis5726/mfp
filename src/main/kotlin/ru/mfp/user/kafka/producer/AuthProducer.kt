package ru.mfp.user.kafka.producer

import ru.mfp.user.entity.User

fun interface AuthProducer {

    fun sendRegistrationEvent(user: User)
}