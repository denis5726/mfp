package ru.mfp.rest

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.mfp.model.JwtAuthentication
import ru.mfp.service.EmailVerificationService

@RestController
@RequestMapping("/emailVerifying")
class EmailVerificationController(
    val service: EmailVerificationService
) {

    @PostMapping("/generate")
    fun generateVerificationCode(authentication: JwtAuthentication) = service.generateVerificationCode(authentication)

    @PostMapping("/verify")
    fun verifyCode(@RequestBody code: String, authentication: JwtAuthentication) = service.verifyCode(code, authentication)
}