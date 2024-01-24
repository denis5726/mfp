package ru.mfp.auth.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.mfp.auth.entity.EmailVerificationCode
import ru.mfp.auth.entity.User
import java.util.*

interface EmailVerificationCodeRepository : JpaRepository<EmailVerificationCode, UUID> {

    fun findFirstByUserOrderByCreatedAtDesc(user: User): EmailVerificationCode?

    fun findByUser(user: User)
}
