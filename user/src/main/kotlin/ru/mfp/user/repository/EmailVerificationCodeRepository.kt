package ru.mfp.user.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.mfp.user.entity.EmailVerificationCode
import ru.mfp.user.entity.User
import java.util.*

interface EmailVerificationCodeRepository : JpaRepository<EmailVerificationCode, UUID> {

    fun findFirstByUserOrderByCreatedAtDesc(user: User): EmailVerificationCode?

    fun findByUser(user: User)
}
