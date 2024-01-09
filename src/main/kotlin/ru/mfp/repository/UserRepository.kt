package ru.mfp.repository

import ru.mfp.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<User, UUID> {

    fun existsByLogin(login: String): Boolean

    fun findByLogin(login: String) : User?
}
