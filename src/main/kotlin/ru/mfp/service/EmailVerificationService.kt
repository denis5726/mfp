package ru.mfp.service

import ru.mfp.dto.EmailVerificationCodeDto
import ru.mfp.model.JwtAuthentication

interface EmailVerificationService {

    fun generateVerificationCode(authentication: JwtAuthentication): EmailVerificationCodeDto

    fun verifyCode(authentication: JwtAuthentication, code: String)
}