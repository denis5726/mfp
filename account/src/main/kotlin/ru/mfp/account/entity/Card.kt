package ru.mfp.account.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.time.ZonedDateTime
import java.util.Currency
import java.util.UUID

@Entity
class Card(
    @Id
    @Column(name = "card_id")
    var id: UUID = UUID.randomUUID(),
    var userId: UUID,
    var bankAccountId: UUID,
    var currency: Currency,
    var createdAt: ZonedDateTime = ZonedDateTime.now()
)
