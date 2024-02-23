package ru.mfp.account.repository

import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import ru.mfp.account.entity.Account

interface AccountRepository : JpaRepository<Account, UUID> {

    fun findByUserIdOrderByCreatedAtDesc(userId: UUID): List<Account>
}
