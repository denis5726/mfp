package ru.mfp.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.mfp.entity.Deposit
import java.util.*

interface DepositRepository : JpaRepository<Deposit, UUID> {

    fun findByAccountUserId(id: UUID): List<Deposit>
}
