package ru.mfp.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import ru.mfp.entity.Account
import java.util.UUID

interface AccountRepository : JpaRepository<Account, UUID> {

    @Query("SELECT a FROM Account a WHERE a.user.id = :userId")
    fun findByUser(userId: UUID): List<Account>
}
