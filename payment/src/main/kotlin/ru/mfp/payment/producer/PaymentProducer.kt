package ru.mfp.payment.producer

import ru.mfp.common.dto.PaymentEventDto

fun interface PaymentProducer {

    fun sendPaymentEvent(event: PaymentEventDto)
}