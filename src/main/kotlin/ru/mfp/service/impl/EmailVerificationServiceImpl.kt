package ru.mfp.service.impl

import org.springframework.stereotype.Service
import ru.mfp.dto.EmailVerificationCodeDto
import ru.mfp.model.JwtAuthentication
import ru.mfp.repository.EmailVerificationCodeRepository
import ru.mfp.service.EmailVerificationService

@Service
class EmailVerificationServiceImpl(
    val repository: EmailVerificationCodeRepository
) : EmailVerificationService {

    override fun generateVerificationCode(authentication: JwtAuthentication): EmailVerificationCodeDto {
        TODO("Not yet implemented")
    }

    override fun verifyCode(authentication: JwtAuthentication, code: String) {
        TODO("Not yet implemented")
    }
}