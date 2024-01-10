package ru.mfp.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import ru.mfp.entity.Card
import java.util.*

interface CardRepository : JpaRepository<Card, UUID> {

    fun findByIdAndUserId(id: UUID, userId: UUID): Optional<Card>

    @Query("SELECT c FROM Card c WHERE c.user.id = :userId")
    fun findByUser(userId: UUID): List<Card>
}
