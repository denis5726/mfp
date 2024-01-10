package ru.mfp.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@Entity
data class AccountHistoryRecord(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "account_history_record_id")
    var id: UUID?,
    @ManyToOne
    @JoinColumn(name = "account_id")
    var account: Account?,
    var amount: BigDecimal?,
    @Enumerated
    var changeReason: AccountChangeReason?,
    var createdAt: LocalDateTime?
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AccountHistoryRecord

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return this::class.simpleName + "(id=$id, amount=$amount, changeReason=$changeReason, createdAt=$createdAt)"
    }
}
