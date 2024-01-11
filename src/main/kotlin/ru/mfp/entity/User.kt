package ru.mfp.entity

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
    open var id: UUID? = null

    @Column(name = "email", nullable = false, unique = true)
    open var email: String? = null

    @Column(name = "password_hash", nullable = false)
    open var passwordHash: String? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, unique = true)
    open var status: UserStatus? = null

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
        other as User

        return id != null && id == other.id
    }

    final override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , email = $email , userStatus = $status )"
    }
}
