package ru.mfp.service.impl

import mu.KotlinLogging
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.mfp.entity.EmailVerificationCode
import ru.mfp.entity.UserStatus
import ru.mfp.exception.EmailVerificationException
import ru.mfp.exception.IllegalServerStateException
import ru.mfp.model.JwtAuthentication
import ru.mfp.repository.EmailVerificationCodeRepository
import ru.mfp.repository.UserRepository
import ru.mfp.service.EmailService
import ru.mfp.service.EmailVerificationService
import kotlin.random.Random

private val log = KotlinLogging.logger { }

@Service
class EmailVerificationServiceImpl(
    private val emailService: EmailService,
    private val repository: EmailVerificationCodeRepository,
    private val userRepository: UserRepository
) : EmailVerificationService {
    private val codeLength: Int = 6
    private val messageSubject: String = "Подтверждение почты для аккаунта MFP"
    private val messageText: String = "Не отвечайте на это письмо!\nВаш код для подтверждения почты: %s\n"
    private val random: Random = Random.Default

    @Transactional
    override fun generateVerificationCode(authentication: JwtAuthentication) {
        log.info { "Generating email verification code for user with id=${authentication.id}" }
        val optionalUser = userRepository.findById(authentication.id)
        if (optionalUser.isEmpty || StringUtils.isBlank(optionalUser.get().email)) {
            throw IllegalServerStateException("User data not found in database")
        }
        val user = optionalUser.get()
        if (user.status != UserStatus.NEW) {
            log.error { "Attempt to verify by verified user, id=${authentication.id}" }
            throw EmailVerificationException("You already verified your email!")
        }
        val code = generateCode()
        val emailVerificationCode = EmailVerificationCode()
        emailVerificationCode.value = code
        emailVerificationCode.user = user
        val savedCode = repository.save(emailVerificationCode)
        log.info { "Generated code: ${savedCode.value}" }
        emailService.sendSimpleTextMessage(user.email, messageSubject, messageText.format(savedCode.value))
        log.info { "Sent email message" }
    }

    override fun verifyCode(code: String, authentication: JwtAuthentication) {
        log.info { "Verifying email code, user.id=${authentication.id}, code=${code}" }
        val user = userRepository.findById(authentication.id)
            .orElseThrow { throw IllegalServerStateException("User data not found in database") }
        if (user.status != UserStatus.NEW) {
            throw EmailVerificationException("You already verified your email by code!")
        }
        val dbCode = repository.findFirstByUserOrderByCreatedAtDesc(user)
            ?: throw EmailVerificationException("Verification code was not created!")
        if (dbCode.value != code) {
            throw EmailVerificationException("Code validation failed!")
        }
        log.info { "User with id=${user.id} was verified" }
        user.status = UserStatus.EMAIL_VERIFIED
        userRepository.save(user)
    }

    private fun generateCode(): String {
        val stringBuilder: StringBuilder = StringBuilder(codeLength)
        for (i in 0 until codeLength) {
            stringBuilder.append(random.nextInt(10))
        }
        return stringBuilder.toString()
    }
}
