package ru.mfp.entity

import jakarta.persistence.*
import org.hibernate.proxy.HibernateProxy
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
    open var id: UUID? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    open var account: Account? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "card_id", nullable = false)
    open var card: Card? = null

    @Column(name = "payment_id", nullable = false, unique = true)
    open var paymentId: UUID? = null

    @Column(name = "operation_id", nullable = false, unique = true)
    open var operationId: UUID? = null

    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
    open var amount: BigDecimal? = null

    @Column(name = "decision", nullable = false)
    open var decision: Boolean? = false

    @Column(name = "description")
    open var description: String? = null

    @Column(name = "created_at", nullable = false)
    open var createdAt: LocalDateTime? = null

    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        val oEffectiveClass =
            if (other is HibernateProxy) other.hibernateLazyInitializer.persistentClass else other.javaClass
        val thisEffectiveClass =
            if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass else this.javaClass
        if (thisEffectiveClass != oEffectiveClass) return false
        other as Deposit

        return id != null && id == other.id
    }

    final override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , paymentId = $paymentId , operationId = $operationId , amount = $amount , decision = $decision , description = $description )"
    }
}
