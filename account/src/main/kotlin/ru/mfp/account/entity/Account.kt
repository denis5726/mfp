package ru.mfp.account.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.util.Currency
import java.util.UUID

@Entity
@Table(name = "account")
class Account(
    @Id
    @Column(name = "account_id")
    var id: UUID = UUID.randomUUID(),
    var userId: UUID,
    var amount: BigDecimal,
    var currency: Currency,
    var createdAt: ZonedDateTime = ZonedDateTime.now()
)