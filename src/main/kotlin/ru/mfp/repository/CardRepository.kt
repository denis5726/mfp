package ru.mfp.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.mfp.entity.Card
import java.util.*

interface CardRepository : JpaRepository<Card, UUID> {

    fun findByIdAndUserId(id: UUID, userId: UUID): Card?

    fun findByUserIdOrderByCreatedAtDesc(userId: UUID): List<Card>
}
