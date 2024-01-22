package ru.mfp.email.service.impl

import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.mfp.auth.entity.EmailVerificationCode
import ru.mfp.email.service.EmailService
import ru.mfp.email.service.EmailVerificationService
import kotlin.random.Random

private val log = KotlinLogging.logger { }

@Service
class EmailVerificationServiceImpl(
    private val emailService: EmailService
) : EmailVerificationService {
    private val codeLength: Int = 6
    private val messageSubject: String = "Подтверждение почты для аккаунта MFP"
    private val messageText: String = "Не отвечайте на это письмо!\nВаш код для подтверждения почты: %s\n"
    private val random: Random = Random.Default

    @Transactional
    override fun sendVerificationCode(email: String): String {
        val code = generateCode()
        val emailVerificationCode = EmailVerificationCode()
        emailVerificationCode.value = code
        log.info { "Generated code: $code" }
        emailService.sendSimpleTextMessage(email, messageSubject, messageText.format(code))
        log.info { "Sent email message" }
        return code
    }

    private fun generateCode(): String {
        val stringBuilder: StringBuilder = StringBuilder(codeLength)
        for (i in 0 until codeLength) {
            stringBuilder.append(random.nextInt(10))
        }
        return stringBuilder.toString()
    }
}
