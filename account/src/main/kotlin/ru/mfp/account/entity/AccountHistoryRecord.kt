package ru.mfp.account.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.util.UUID

@Entity
class AccountHistoryRecord(
    @Id
    @Column(name = "account_history_record_id")
    var id: UUID = UUID.randomUUID(),
    @ManyToOne
    @JoinColumn(name = "account_id")
    var account: Account,
    var diff: BigDecimal,
    @Enumerated(EnumType.STRING)
    var changeReason: AccountChangeReason,
    var createdAt: ZonedDateTime = ZonedDateTime.now()
)
