package ru.mfp.auth.kafka.producer

import ru.mfp.auth.entity.User

fun interface AuthProducer {

    fun sendRegistrationEvent(user: User)
}