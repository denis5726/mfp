package ru.mfp.service

import ru.mfp.model.JwtAuthentication

interface EmailVerificationService {

    fun generateVerificationCode(authentication: JwtAuthentication)

    fun verifyCode(code: String, authentication: JwtAuthentication)
}