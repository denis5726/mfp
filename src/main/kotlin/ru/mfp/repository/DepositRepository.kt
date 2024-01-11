package ru.mfp.repository

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import ru.mfp.entity.Deposit
import java.util.*

interface DepositRepository : JpaRepository<Deposit, UUID> {

    @EntityGraph(attributePaths = ["card", "account"])
    fun findByAccountUserIdOrderByCreatedAtDesc(id: UUID, pageable: Pageable): Slice<Deposit>
}
