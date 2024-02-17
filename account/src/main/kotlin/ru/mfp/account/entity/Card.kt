package ru.mfp.account.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime
import java.util.Currency
import java.util.UUID
import org.hibernate.annotations.CreationTimestamp

@Entity
class Card(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "card_id")
    var id: UUID,
    var userId: UUID,
    var bankAccountId: UUID,
    var currency: Currency,
    @CreationTimestamp
    var createdAt: LocalDateTime
)
