package ru.mfp.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import java.util.*

@Entity
data class Card(
    @Id
    @Column(name = "card_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID?,
    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: User?,
    var accountId: UUID?,
    @CreationTimestamp
    var createdAt: LocalDateTime?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (other !is Card) return false

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String {
        return this::class.simpleName + "(id=$id, accountId=$accountId, createdAt=$createdAt)"
    }
}
