package ru.mfp.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.mfp.entity.EmailVerificationCode
import ru.mfp.entity.User
import java.util.*

interface EmailVerificationCodeRepository : JpaRepository<EmailVerificationCode, UUID> {

    fun findFirstByUserOrderByCreatedAtDesc(user: User): Optional<EmailVerificationCode>
}