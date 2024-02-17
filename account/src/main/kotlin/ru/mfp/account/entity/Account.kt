package ru.mfp.account.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.util.Currency
import java.util.UUID
import org.hibernate.annotations.CreationTimestamp

@Entity
@Table(name = "account")
class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "account_id")
    var id: UUID,
    var userId: UUID,
    var amount: BigDecimal,
    var currency: Currency,
    @CreationTimestamp
    var createdAt: ZonedDateTime
)