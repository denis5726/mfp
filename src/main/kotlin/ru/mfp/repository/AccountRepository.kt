package ru.mfp.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.mfp.entity.Account
import java.util.UUID

interface AccountRepository : JpaRepository<Account, UUID> {
    
    fun findByUserId(userId: UUID): List<Account>
}
