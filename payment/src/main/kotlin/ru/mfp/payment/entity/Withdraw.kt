package ru.mfp.payment.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.util.UUID

@Entity
class Withdraw(
    @Id
    @Column(name = "withdraw_id")
    var id: UUID = UUID.randomUUID(),
    var accountId: UUID,
    var cardId: UUID,
    var paymentId: UUID,
    var operationId: UUID,
    var amount: BigDecimal,
    var createdAt: ZonedDateTime = ZonedDateTime.now()
)
