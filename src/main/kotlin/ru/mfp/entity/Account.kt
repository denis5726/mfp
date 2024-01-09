package ru.mfp.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@Entity
data class Account(
    @Id
    @Column(name = "account_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID?,
    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: User?,
    var amount: BigDecimal?,
    var currency: Currency?,
    @CreatedDate
    var createdAt: LocalDateTime?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (other !is Account) return false

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String {
        return this::class.simpleName + "(user = $user , amount = $amount , currency = $currency )"
    }
}
