package ru.mfp.deposit.kafka.producer

import ru.mfp.deposit.dto.DepositCreatingRequestDto
import ru.mfp.deposit.entity.Deposit
import java.util.*

interface DepositProducer {

    fun sendDepositCreatedEvent(deposit: Deposit)

    fun sendDepositErrorEvent(request: DepositCreatingRequestDto, userId: UUID, message: String?, paymentId: UUID)
}