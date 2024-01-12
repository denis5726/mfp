package ru.mfp.card.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.mfp.card.entity.Card
import java.util.*

interface CardRepository : JpaRepository<Card, UUID> {

    fun findByIdAndUserId(id: UUID, userId: UUID): Card?

    fun findByUserIdOrderByCreatedAtDesc(userId: UUID): List<Card>
}
