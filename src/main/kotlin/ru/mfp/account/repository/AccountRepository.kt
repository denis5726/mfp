package ru.mfp.account.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.mfp.account.entity.Account
import java.util.*

interface AccountRepository : JpaRepository<Account, UUID> {

    fun findByUserIdOrderByCreatedAtDesc(userId: UUID): List<Account>
}
