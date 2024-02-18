package ru.mfp.verification.service.impl

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.Currency
import java.util.UUID
import kotlin.random.Random
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.mfp.account.service.CardService
import ru.mfp.common.model.JwtAuthentication
import ru.mfp.common.model.UserRole
import ru.mfp.email.service.EmailVerificationService
import ru.mfp.payment.client.PaymentApiClientService
import ru.mfp.payment.dto.PaymentCreatingRequestDto
import ru.mfp.payment.exception.PaymentServiceApiException
import ru.mfp.user.service.UserService
import ru.mfp.verification.entity.EmailVerificationCode
import ru.mfp.verification.exception.VerificationException
import ru.mfp.verification.repository.EmailVerificationCodeRepository
import ru.mfp.verification.service.VerificationService

private val log = KotlinLogging.logger { }

@Service
@Transactional
class VerificationServiceImpl(
    private val emailVerificationService: EmailVerificationService,
    private val repository: EmailVerificationCodeRepository,
    private val userService: UserService,
    private val cardService: CardService,
    private val paymentApiClientService: PaymentApiClientService
) : VerificationService {
    @Value("\${mfp.payment-service.main-bank-account-id}")
    private lateinit var mainBankAccountId: UUID
    private val verificationAmount = BigDecimal.valueOf(1)
    private val verificationCurrency = Currency.getInstance("RUB")
    private val codeLength: Int = 6
    private val random: Random = Random.Default

    override fun generateEmailCode(authentication: JwtAuthentication) {
        log.info { "Generating email verification code for user with id=${authentication.id}" }
        val user = userService.findById(authentication.id)
        if (user.role != UserRole.NEW.toString()) {
            log.error { "Attempt to verify by verified user, id=${authentication.id}" }
            throw VerificationException("You already verified your email!")
        }
        val code = generateCode()
        emailVerificationService.sendVerificationCode(user.email, code)
        repository.save(EmailVerificationCode(userId = user.id, value = code))
    }

    override fun verifyEmailCode(code: String, authentication: JwtAuthentication) {
        log.info { "Verifying email code, user.id=${authentication.id}, code=${code}" }
        val user = userService.findById(authentication.id)
        if (user.role != UserRole.NEW.toString()) {
            throw VerificationException("You already verified your email by code!")
        }
        val dbCode = repository.findFirstByUserIdOrderByCreatedAtDesc(user.id)
            ?: throw VerificationException("Verification code was not created!")
        if (dbCode.value != code) {
            throw VerificationException("Code validation failed!")
        }
        log.info { "User with id=${user.id} was verified" }
        userService.updateRole(user.id, UserRole.EMAIL_VERIFIED.toString())
    }

    // В будущем часть нужно вынести в модуль payment (публиковать событие о проведённой проверке)
    override fun verifySolvency(cardId: UUID, authentication: JwtAuthentication) {
        log.info { "Verifying solvency (cardId=$cardId, userId=${authentication.id}" }
        val user = userService.findById(authentication.id)
        if (user.role != UserRole.EMAIL_VERIFIED.toString()) {
            log.error { "Attempt to verify solvency with role: ${user.role}, userId=${user.id}" }
            throw VerificationException("Invalid role for this action")
        }
        val card = cardService.findCardById(cardId, authentication)
        val paymentRequestDto = PaymentCreatingRequestDto(
            UUID.randomUUID(),
            card.bankAccountId,
            mainBankAccountId,
            verificationAmount.toPlainString(),
            verificationCurrency.currencyCode,
            LocalDateTime.now()
        )
        val paymentDto = try {
            paymentApiClientService.createPayment(paymentRequestDto)
        } catch (e: PaymentServiceApiException) {
            log.error { "Payment service has return an error: ${e.message}" }
            throw VerificationException("Payment service error: ${e.message}")
        }
        if (!paymentDto.decision) {
            log.error { "Payment service has declined a payment: ${paymentDto.description}" }
            throw VerificationException("Payment service has rejected a payment: ${paymentDto.description}")
        }
        log.info { "Solvency verifying payment success: $paymentDto" }
        userService.updateRole(user.id, UserRole.SOLVENCY_VERIFIED.toString())
        val refundPaymentRequestDto = PaymentCreatingRequestDto(
            UUID.randomUUID(),
            mainBankAccountId,
            card.bankAccountId,
            verificationAmount.toPlainString(),
            verificationCurrency.currencyCode,
            LocalDateTime.now()
        )
        log.info { "User role updated (userid=${user.id}), refunding the payment" }
        try {
            paymentApiClientService.createPayment(refundPaymentRequestDto)
        } catch (e: PaymentServiceApiException) {
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
