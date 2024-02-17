package ru.mfp.account.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.util.UUID
import org.hibernate.annotations.CreationTimestamp

@Entity
class AccountHistoryRecord(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "account_history_record_id")
    var id: UUID,
    @ManyToOne
    @JoinColumn(name = "account_id")
    var account: Account,
    var diff: BigDecimal,
    @Enumerated(EnumType.STRING)
    var changeReason: AccountChangeReason,
    @CreationTimestamp
    var createdAt: ZonedDateTime
)
