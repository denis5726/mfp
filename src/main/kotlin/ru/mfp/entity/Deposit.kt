package ru.mfp.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@Entity
data class Deposit(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "deposit_id")
    var id: UUID?,
    @ManyToOne
    @JoinColumn(name = "account_id")
    var account: Account?,
    @ManyToOne
    @JoinColumn(name = "card_id")
    var card: Card?,
    var paymentId: UUID,
    var operationId: UUID,
    var amount: BigDecimal?,
    var decision: Boolean?,
    var description: String?,
    var createdAt: LocalDateTime?
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Deposit

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return this::class.simpleName + "(id=$id, paymentId=$paymentId, operationId=$operationId, amount=$amount, decision=$decision, description=$description, createdAt=$createdAt)"
    }
}
