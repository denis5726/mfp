package ru.mfp.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.proxy.HibernateProxy
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "email_verification_code")
@Suppress("kotlin:S2097")
open class EmailVerificationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "email_verification_code_id", nullable = false)
    open var id: UUID? = null

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    open var user: User? = null

    @Column(name = "value", nullable = false, length = 10)
    open var value: String? = null

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
        other as EmailVerificationCode

        return id != null && id == other.id
    }

    final override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , value = $value )"
    }
}
