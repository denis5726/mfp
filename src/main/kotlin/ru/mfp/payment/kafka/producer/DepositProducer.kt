package ru.mfp.payment.kafka.producer

import ru.mfp.payment.dto.DepositCreatingRequestDto
import ru.mfp.payment.entity.Deposit
import java.util.*

interface DepositProducer {

    fun sendDepositCreatedEvent(deposit: Deposit)

    fun sendDepositErrorEvent(
        request: DepositCreatingRequestDto,
        userId: UUID,
        message: String?,
        paymentId: UUID,
        currency: Currency
    )
}