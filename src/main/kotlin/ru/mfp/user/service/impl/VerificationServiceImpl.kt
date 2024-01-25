package ru.mfp.user.service.impl

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.mfp.user.entity.EmailVerificationCode
import ru.mfp.common.model.UserRole
import ru.mfp.user.exception.VerificationException
import ru.mfp.user.repository.EmailVerificationCodeRepository
import ru.mfp.user.repository.UserRepository
import ru.mfp.user.service.VerificationService
import ru.mfp.account.service.CardService
import ru.mfp.common.exception.IllegalServerStateException
import ru.mfp.common.model.JwtAuthentication
import ru.mfp.payment.exception.PaymentApiException
import ru.mfp.email.service.EmailVerificationService
import ru.mfp.payment.client.PaymentApiClientService
import ru.mfp.payment.dto.PaymentCreatingRequestDto
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*
import kotlin.random.Random

private val log = KotlinLogging.logger { }

@Service
@Transactional
class VerificationServiceImpl(
    private val emailVerificationService: EmailVerificationService,
    private val repository: EmailVerificationCodeRepository,
    private val userRepository: UserRepository,
    private val cardService: CardService,
    private val paymentApiClientService: PaymentApiClientService
) : VerificationService {
    @Value("\${mfp.payment.main-bank-account-id}")
    private var mainBankAccountId: UUID? = null
    private val verificationAmount = BigDecimal.valueOf(1)
    private val verificationCurrency = Currency.getInstance("RUB")
    private val codeLength: Int = 6
    private val random: Random = Random.Default

    override fun generateEmailCode(authentication: JwtAuthentication) {
        log.info { "Generating email verification code for user with id=${authentication.id}" }
        val user = userRepository.findById(authentication.id).orElseThrow {
            throw IllegalServerStateException("User data not found in database")
        }
        if (user.role != UserRole.NEW) {
            log.error { "Attempt to verify by verified user, id=${authentication.id}" }
            throw VerificationException("You already verified your email!")
        }
        val code = generateCode()
        emailVerificationService.sendVerificationCode(user.email, code)
        val emailVerificationCode = EmailVerificationCode()
        emailVerificationCode.value = code
        emailVerificationCode.user = user
        repository.save(emailVerificationCode)
    }

    override fun verifyEmailCode(code: String, authentication: JwtAuthentication) {
        log.info { "Verifying email code, user.id=${authentication.id}, code=${code}" }
        val user = userRepository.findById(authentication.id)
            .orElseThrow { throw IllegalServerStateException("User data not found in database") }
        if (user.role != UserRole.NEW) {
            throw VerificationException("You already verified your email by code!")
        }
        val dbCode = repository.findFirstByUserOrderByCreatedAtDesc(user)
            ?: throw VerificationException("Verification code was not created!")
        if (dbCode.value != code) {
            throw VerificationException("Code validation failed!")
        }
        log.info { "User with id=${user.id} was verified" }
        user.role = UserRole.EMAIL_VERIFIED
        userRepository.save(user)
    }

    override fun verifySolvency(cardId: UUID, authentication: JwtAuthentication) {
        log.info { "Verifying solvency (cardId=$cardId, userId=${authentication.id}" }
        val user = userRepository.findById(authentication.id)
            .orElseThrow { throw IllegalServerStateException("User not found") }
        if (user.role != UserRole.EMAIL_VERIFIED) {
            log.error { "Attempt to verify solvency with role: ${user.role}, userId=${user.id}" }
            throw VerificationException("Invalid role for this action")
        }
        val card = cardService.findCardById(cardId, authentication)
        val paymentRequestDto = PaymentCreatingRequestDto(
            UUID.randomUUID(),
            card.bankAccountId,
            mainBankAccountId ?: run {
                log.error { "Main bank account id is not provided!" }
                throw IllegalServerStateException("Internal server error")
            },
            verificationAmount.toPlainString(),
            verificationCurrency.currencyCode,
            LocalDateTime.now()
        )
        val paymentDto = try {
            paymentApiClientService.createPayment(paymentRequestDto)
        } catch (e: PaymentApiException) {
            log.error { "Payment service has return an error: ${e.message}" }
            throw VerificationException("Payment service error: ${e.message}")
        }
        if (!paymentDto.decision) {
            log.error { "Payment service has declined a payment: ${paymentDto.description}" }
            throw VerificationException("Payment service has rejected a payment: ${paymentDto.description}")
        }
        log.info { "Solvency verifying payment success: $paymentDto" }
        user.role = UserRole.SOLVENCY_VERIFIED
        userRepository.saveAndFlush(user)
        val refundPaymentRequestDto = PaymentCreatingRequestDto(
            UUID.randomUUID(),
            mainBankAccountId ?: run {
                log.error { "Main bank account id is not provided!" }
                throw IllegalServerStateException("Internal server error")
            },
            card.bankAccountId,
            verificationAmount.toPlainString(),
            verificationCurrency.currencyCode,
            LocalDateTime.now()
        )
        log.info { "User role updated (userid=${user.id}), refunding the payment" }
        try {
            paymentApiClientService.createPayment(refundPaymentRequestDto)
        } catch (e: PaymentApiException) {
            log.error { "Exception during refund verification payment: ${e.message}" }
        }
    }

    private fun generateCode(): String {
        val stringBuilder: StringBuilder = StringBuilder(codeLength)
        for (i in 0 until codeLength) {
            stringBuilder.append(random.nextInt(10))
        }
        return stringBuilder.toString()
    }
}
