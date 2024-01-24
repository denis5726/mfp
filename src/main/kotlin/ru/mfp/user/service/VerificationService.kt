package ru.mfp.user.service

import ru.mfp.common.model.JwtAuthentication
import java.util.UUID

interface VerificationService {

    fun generateEmailCode(authentication: JwtAuthentication)

    fun verifyEmailCode(code: String, authentication: JwtAuthentication)

    fun verifySolvency(cardId: UUID, authentication: JwtAuthentication)
}
