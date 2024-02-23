package ru.mfp.verification.service

import java.util.UUID
import ru.mfp.common.model.JwtAuthentication

interface VerificationService {

    fun generateEmailCode(authentication: JwtAuthentication)

    fun verifyEmailCode(code: String, authentication: JwtAuthentication)

    fun verifySolvency(cardId: UUID, authentication: JwtAuthentication)
}
