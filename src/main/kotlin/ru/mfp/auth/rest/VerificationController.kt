package ru.mfp.auth.rest

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.mfp.auth.service.VerificationService
import ru.mfp.common.model.JwtAuthentication
import java.util.*

@RestController
@RequestMapping("/verification")
class VerificationController(
    private val verificationService: VerificationService
) {

    @PostMapping("/email/generate")
    fun generateEmailCode(authentication: JwtAuthentication) = verificationService.generateEmailCode(authentication)

    @PostMapping("/email/verify")
    fun verifyEmailCode(@RequestBody code: String, authentication: JwtAuthentication) =
        verificationService.verifyEmailCode(code, authentication)

    @PostMapping("/solvency")
    fun verifySolvency(@RequestBody cardId: UUID, authentication: JwtAuthentication) =
        verificationService.verifySolvency(cardId, authentication)
}
