package ru.mfp.payment.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID
import org.hibernate.annotations.CreationTimestamp

@Entity
class Withdraw(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "withdraw_id")
    var id: UUID,
    var accountId: UUID,
    @ManyToOne(optional = false)
    @JoinColumn(name = "card_id", nullable = false)
    var cardId: UUID,
    @Column(name = "payment_id", nullable = false, unique = true)
    var paymentId: UUID,
    @Column(name = "operation_id", nullable = false, unique = true)
    var operationId: UUID,
    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
    var amount: BigDecimal,
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime
)
