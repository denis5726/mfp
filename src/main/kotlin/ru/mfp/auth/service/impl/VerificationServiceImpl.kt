package ru.mfp.auth.service.impl

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.mfp.auth.entity.EmailVerificationCode
import ru.mfp.auth.entity.UserStatus
import ru.mfp.auth.exception.VerificationException
import ru.mfp.auth.repository.EmailVerificationCodeRepository
import ru.mfp.auth.repository.UserRepository
import ru.mfp.auth.service.VerificationService
import ru.mfp.card.service.CardService
import ru.mfp.common.exception.IllegalServerStateException
import ru.mfp.common.model.JwtAuthentication
import ru.mfp.deposit.exception.PaymentApiException
import ru.mfp.email.service.EmailVerificationService
import ru.mfp.payment.PaymentApiClientService
import ru.mfp.payment.dto.PaymentCreatingRequestDto
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

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

    override fun generateEmailCode(authentication: JwtAuthentication) {
        log.info { "Generating email verification code for user with id=${authentication.id}" }
        val user = userRepository.findById(authentication.id).orElseThrow {
            throw IllegalServerStateException("User data not found in database")
        }
        if (user.status != UserStatus.NEW) {
            log.error { "Attempt to verify by verified user, id=${authentication.id}" }
            throw VerificationException("You already verified your email!")
        }
        val code = emailVerificationService.sendVerificationCode(user.email)
        val emailVerificationCode = EmailVerificationCode()
        emailVerificationCode.value = code
        emailVerificationCode.user = user
        repository.save(emailVerificationCode)
    }

    override fun verifyEmailCode(code: String, authentication: JwtAuthentication) {
        log.info { "Verifying email code, user.id=${authentication.id}, code=${code}" }
        val user = userRepository.findById(authentication.id)
            .orElseThrow { throw IllegalServerStateException("User data not found in database") }
        if (user.status != UserStatus.NEW) {
            throw VerificationException("You already verified your email by code!")
        }
        val dbCode = repository.findFirstByUserOrderByCreatedAtDesc(user)
            ?: throw VerificationException("Verification code was not created!")
        if (dbCode.value != code) {
            throw VerificationException("Code validation failed!")
        }
        log.info { "User with id=${user.id} was verified" }
        user.status = UserStatus.EMAIL_VERIFIED
        userRepository.save(user)
    }

    override fun verifySolvency(cardId: UUID, authentication: JwtAuthentication) {
        log.info { "Verifying solvency (cardId=$cardId, userId=${authentication.id}" }
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
        val user = userRepository.findById(authentication.id).orElseThrow { throw IllegalServerStateException("User not found") }
        user.status = UserStatus.SOLVENCY_VERIFIED
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
        log.info { "User status updated (userid=${user.id}), refunding the payment" }
        try {
            paymentApiClientService.createPayment(refundPaymentRequestDto)
        } catch (e: PaymentApiException) {
            log.error { "Exception during refund verification payment: ${e.message}" }
        }
    }
}
