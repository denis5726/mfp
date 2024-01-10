package ru.mfp.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import java.util.*

@Entity
data class EmailVerificationCode(
    @Id
    @Column(name = "email_verification_code_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID?,
    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: User?,
    var value: String?,
    @CreationTimestamp
    var createdAt: LocalDateTime?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (other !is EmailVerificationCode) return false

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
    override fun toString(): String {
        return this::class.simpleName + "(id=$id, value=$value, createdAt=$createdAt)"
    }
}
