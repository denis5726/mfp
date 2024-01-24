package ru.mfp.payment.repository

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import ru.mfp.payment.entity.Withdraw
import java.util.*

interface WithdrawRepository : JpaRepository<Withdraw, UUID> {

    @EntityGraph(attributePaths = ["card", "account"])
    fun findByAccountUserIdOrderByCreatedAtDesc(id: UUID, pageable: Pageable): Slice<Withdraw>

    fun findByPaymentId(paymentId: UUID): Withdraw?
}
