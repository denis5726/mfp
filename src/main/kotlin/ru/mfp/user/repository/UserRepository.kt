package ru.mfp.user.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.mfp.user.entity.User
import java.util.*

interface UserRepository : JpaRepository<User, UUID> {

    fun existsByEmail(email: String): Boolean

    fun findByEmail(email: String): User?
}
