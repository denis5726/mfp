package ru.mfp.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.proxy.HibernateProxy
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "account")
@Suppress("kotlin:S2097")
open class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "account_id", nullable = false)
    open var id: UUID? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    open var user: User? = null

    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
    open var amount: BigDecimal? = null

    @Column(name = "currency", nullable = false)
    open var currency: Currency? = null

    @CreationTimestamp
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
        other as Account

        return id != null && id == other.id
    }

    final override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , amount = $amount , currency = $currency )"
    }
}
