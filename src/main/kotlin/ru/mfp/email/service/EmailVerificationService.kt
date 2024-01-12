package ru.mfp.email.service

import ru.mfp.common.model.JwtAuthentication

interface EmailVerificationService {

    fun generateVerificationCode(authentication: JwtAuthentication)

    fun verifyCode(code: String, authentication: JwtAuthentication)
}
