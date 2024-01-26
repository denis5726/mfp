package ru.mfp.payment.kafka.producer

import ru.mfp.payment.dto.PaymentEventDto

fun interface PaymentProducer {

    fun sendPaymentEvent(event: PaymentEventDto)
}