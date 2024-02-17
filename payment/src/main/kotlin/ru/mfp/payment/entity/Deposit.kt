package ru.mfp.payment.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.util.UUID
import org.hibernate.annotations.CreationTimestamp

@Entity
class Deposit(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "deposit_id")
    var id: UUID,
    var accountId: UUID,
    var cardId: UUID,
    var paymentId: UUID,
    var operationId: UUID,
    var amount: BigDecimal,
    @CreationTimestamp
    var createdAt: ZonedDateTime
)
