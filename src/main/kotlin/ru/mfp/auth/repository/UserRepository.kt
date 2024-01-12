package ru.mfp.auth.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.mfp.auth.entity.User
import java.util.*

interface UserRepository : JpaRepository<User, UUID> {

    fun existsByEmail(email: String): Boolean

    fun findByEmail(email: String): User?
}
