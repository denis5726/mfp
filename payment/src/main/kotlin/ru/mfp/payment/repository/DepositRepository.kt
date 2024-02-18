package ru.mfp.payment.repository

import java.math.BigDecimal
import java.util.*
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import ru.mfp.payment.entity.Deposit

interface DepositRepository : JpaRepository<Deposit, UUID> {

    fun findByAccountIdInOrderByCreatedAtDesc(accountIds: List<UUID>, pageable: Pageable): Slice<Deposit>

    fun findByPaymentId(paymentId: UUID): Deposit?

    @Query("SELECT COALESCE(SUM(d.amount), 0) FROM Deposit d WHERE d.account.user.id = :userId")
    fun findSumOfAllDepositAmounts(userId: UUID): BigDecimal
}
