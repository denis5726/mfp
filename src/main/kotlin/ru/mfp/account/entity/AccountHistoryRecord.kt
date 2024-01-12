package ru.mfp.account.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.proxy.HibernateProxy
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "account_history_record")
@Suppress("kotlin:S2097")
open class AccountHistoryRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "account_history_record_id", nullable = false)
    open lateinit var id: UUID

    @ManyToOne(optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    open lateinit var account: Account

    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
    open lateinit var amount: BigDecimal

    @Enumerated(EnumType.STRING)
    @Column(name = "change_reason", nullable = false, length = 50)
    open lateinit var changeReason: AccountChangeReason

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
        other as AccountHistoryRecord

        return id == other.id
    }

    final override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , amount = $amount , changeReason = $changeReason )"
    }
}
