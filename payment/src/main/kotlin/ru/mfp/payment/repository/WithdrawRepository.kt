package ru.mfp.payment.repository

import java.util.UUID
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import ru.mfp.payment.entity.Withdraw

interface WithdrawRepository : JpaRepository<Withdraw, UUID> {

    fun findByAccountIdInOrderByCreatedAtDesc(accountIds: List<UUID>, pageable: Pageable): Slice<Withdraw>
}
