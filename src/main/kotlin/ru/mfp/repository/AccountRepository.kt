package ru.mfp.repository

import ru.mfp.entity.Account
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface AccountRepository : JpaRepository<Account, UUID> {

    fun existsByLogin(login: String): Boolean

    fun findByLogin(login: String) : Account?
}
