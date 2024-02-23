package ru.mfp.account.repository

import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import ru.mfp.account.entity.Card

interface CardRepository : JpaRepository<Card, UUID> {

    fun findByIdAndUserId(id: UUID, userId: UUID): Card?

    fun findByUserIdOrderByCreatedAtDesc(userId: UUID): List<Card>
}
