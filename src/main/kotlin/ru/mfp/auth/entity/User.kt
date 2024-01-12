package ru.mfp.auth.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.proxy.HibernateProxy
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "`user`")
@Suppress("kotlin:S2097")
open class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", nullable = false)
    open lateinit var id: UUID

    @Column(name = "email", nullable = false, unique = true)
    open lateinit var email: String

    @Column(name = "password_hash", nullable = false)
    open lateinit var passwordHash: String

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, unique = true)
    open lateinit var status: UserStatus

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
        other as User

        return id == other.id
    }

    final override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , email = $email , userStatus = $status )"
    }
}
