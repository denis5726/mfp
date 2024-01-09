package ru.mfp.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime
import java.util.UUID

@Entity
data class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID?,
    var login: String?,
    var passwordHash: String?,
    @CreatedDate
    var registeredAt: LocalDateTime?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (other !is Account) return false

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String {
        return this::class.simpleName + "(id = $id , login = $login , passwordHash = $passwordHash , registeredAt = $registeredAt )"
    }
}
