package ru.mfp.repository

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import ru.mfp.entity.Deposit
import java.util.*

interface DepositRepository : JpaRepository<Deposit, UUID> {

    fun findByAccountUserIdOrderByCreatedAtDesc(id: UUID, pageable: Pageable): Slice<Deposit>
}
