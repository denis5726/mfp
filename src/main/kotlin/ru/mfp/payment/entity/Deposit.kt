package ru.mfp.payment.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.proxy.HibernateProxy
import ru.mfp.account.entity.Account
import ru.mfp.account.entity.Card
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "deposit")
@Suppress("kotlin:S2097")
open class Deposit {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "deposit_id", nullable = false)
    open lateinit var id: UUID

    @ManyToOne(optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    open lateinit var account: Account

    @ManyToOne(optional = false)
    @JoinColumn(name = "card_id", nullable = false)
    open lateinit var card: Card

    @Column(name = "payment_id", nullable = false, unique = true)
    open lateinit var paymentId: UUID

    @Column(name = "operation_id", nullable = false, unique = true)
    open lateinit var operationId: UUID

    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
    open lateinit var amount: BigDecimal

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    open lateinit var createdAt: LocalDateTime

    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        val oEffectiveClass =
            if (other is HibernateProxy) other.hibernateLazyInitializer.persistentClass else other.javaClass
        val thisEffectiveClass =
            if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass else this.javaClass
        if (thisEffectiveClass != oEffectiveClass) return false
        other as Deposit

        return id == other.id
    }

    final override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , paymentId = $paymentId , operationId = $operationId , amount = $amount  )"
    }
}
