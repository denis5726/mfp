package ru.mfp.payment.service.impl

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.Currency
import java.util.UUID
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.mfp.account.service.CardService
import ru.mfp.common.model.JwtAuthentication
import ru.mfp.payment.client.PaymentApiClientService
import ru.mfp.payment.dto.PaymentCreatingRequestDto
import ru.mfp.payment.exception.PaymentCreatingException
import ru.mfp.payment.exception.PaymentServiceApiException
import ru.mfp.payment.service.SolvencyVerificationPaymentService

private val log = KotlinLogging.logger { }

@Service
class SolvencyVerificationPaymentServiceImpl(
    private val cardService: CardService,
    private val paymentApiClientService: PaymentApiClientService
) : SolvencyVerificationPaymentService {
    @Value("\${mfp.payment-service.main-bank-account-id}")
    private lateinit var mainBankAccountId: UUID
    private val verificationAmount = BigDecimal.valueOf(1)
    private val verificationCurrency = Currency.getInstance("RUB")

    override fun doVerificationPayment(cardId: UUID, authentication: JwtAuthentication) {
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
            throw PaymentCreatingException("Payment service error: ${e.message}")
        }
        if (!paymentDto.decision) {
            log.error { "Payment service has declined a payment: ${paymentDto.description}" }
            throw PaymentCreatingException("Payment service has rejected a payment: ${paymentDto.description}")
        }
        log.info { "Solvency verifying payment success: $paymentDto" }
        val refundPaymentRequestDto = PaymentCreatingRequestDto(
            UUID.randomUUID(),
            mainBankAccountId,
            card.bankAccountId,
            verificationAmount.toPlainString(),
            verificationCurrency.currencyCode,
            LocalDateTime.now()
        )
        try {
            paymentApiClientService.createPayment(refundPaymentRequestDto)
        } catch (e: PaymentServiceApiException) {
            log.error { "Exception during refund verification payment: ${e.message}" }
        }
    }
}