package ru.mfp.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.mfp.entity.EmailVerificationCode
import java.util.*

interface EmailVerificationCodeRepository : JpaRepository<EmailVerificationCode, UUID>