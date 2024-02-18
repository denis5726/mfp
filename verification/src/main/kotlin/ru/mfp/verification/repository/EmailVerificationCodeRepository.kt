package ru.mfp.verification.repository

import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import ru.mfp.verification.entity.EmailVerificationCode

interface EmailVerificationCodeRepository : JpaRepository<EmailVerificationCode, UUID> {

    fun findFirstByUserIdOrderByCreatedAtDesc(userId: UUID): EmailVerificationCode?
}
