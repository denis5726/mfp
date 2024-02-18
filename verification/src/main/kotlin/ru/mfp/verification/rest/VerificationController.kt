package ru.mfp.verification.rest

import java.util.*
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.mfp.common.config.security.aop.EmailVerified
import ru.mfp.common.config.security.aop.New
import ru.mfp.common.model.JwtAuthentication
import ru.mfp.verification.service.VerificationService

@RestController
@RequestMapping("/verification")
class VerificationController(
    private val verificationService: VerificationService
) {

    @PostMapping("/email/generate")
    @New
    fun generateEmailCode(authentication: JwtAuthentication) = verificationService.generateEmailCode(authentication)

    @PostMapping("/email/verify")
    @New
    fun verifyEmailCode(@RequestBody code: String, authentication: JwtAuthentication) =
        verificationService.verifyEmailCode(code, authentication)

    @PostMapping("/solvency")
    @EmailVerified
    fun verifySolvency(@RequestBody cardId: UUID, authentication: JwtAuthentication) =
        verificationService.verifySolvency(cardId, authentication)
}
