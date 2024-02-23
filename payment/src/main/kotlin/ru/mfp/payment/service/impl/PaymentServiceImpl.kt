package ru.mfp.payment.service.impl

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.Currency
import java.util.UUID
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import ru.mfp.account.service.AccountService
import ru.mfp.account.service.CardService
import ru.mfp.common.dto.PaymentEventDto
import ru.mfp.common.model.JwtAuthentication
import ru.mfp.payment.client.PaymentApiClientService
import ru.mfp.payment.dto.PaymentCreatingRequestDto
import ru.mfp.payment.exception.PaymentCreatingException
import ru.mfp.payment.exception.PaymentServiceApiException
import ru.mfp.payment.model.PaymentCreatingResult
import ru.mfp.payment.producer.PaymentProducer
import ru.mfp.payment.service.PaymentService

private val log = KotlinLogging.logger { }

@Component
class PaymentServiceImpl(
    private val paymentApiClientService: PaymentApiClientService,
    private val cardRepository: CardService,
    private val accountRepository: AccountService,
    private val paymentProducer: PaymentProducer,
) : PaymentService {
    @Value("\${mfp.payment-service.main-bank-account-id}")
    private lateinit var mainBankAccountId: UUID

    override fun doPayment(
        authentication: JwtAuthentication,
        accountId: UUID,
        cardId: UUID,
        amount: BigDecimal,
        toBank: Boolean
    ): PaymentCreatingResult {
        val card = cardRepository.findCardById(cardId, authentication)
        val account = accountRepository.findAccounts(authentication)
            .firstOrNull { it.id == accountId }
            ?: throw PaymentCreatingException("Account with id=$accountId is not found")
        if (account.currency != card.currency) {
            throw PaymentCreatingException("Account and card currencies is not equal!")
        }
        val paymentRequest = buildAbstractRequest(
            if (toBank) card.bankAccountId else mainBankAccountId,
            if (toBank) mainBankAccountId else card.bankAccountId,
            amount,
            Currency.getInstance(card.currency)
        )
        val paymentType = if (toBank) PaymentEventDto.PaymentType.DEPOSIT else PaymentEventDto.PaymentType.WITHDRAW
        val paymentDto = try {
            paymentApiClientService.createPayment(paymentRequest)
        } catch (e: PaymentServiceApiException) {
            paymentProducer.sendPaymentEvent(
                PaymentEventDto(
                    authentication.id,
                    paymentRequest.id,
                    null,
                    accountId,
                    cardId,
                    false,
                    e.message,
                    amount.toString(),
                    card.currency,
                    paymentType
                )
            )
            throw PaymentCreatingException("Payment service error: ${e.message}")
        }
        val event = PaymentEventDto(
            authentication.id,
            paymentRequest.id,
            paymentDto.operationId,
            accountId,
            cardId,
            paymentDto.decision,
            paymentDto.description,
            amount.toString(),
            card.currency,
            paymentType
        )

        if (!paymentDto.decision) {
            log.error { "Payment ${paymentDto.paymentId} declined" }
            paymentProducer.sendPaymentEvent(event)
            throw PaymentCreatingException("Payment declined: ${paymentDto.description}")
        }
        return PaymentCreatingResult(paymentDto) { paymentProducer.sendPaymentEvent(event) }
    }

    private fun buildAbstractRequest(idFrom: UUID, idTo: UUID, amount: BigDecimal, currency: Currency) =
        PaymentCreatingRequestDto(
            UUID.randomUUID(),
            idFrom,
            idTo,
            amount.toString(),
            currency.currencyCode,
            LocalDateTime.now()
        )
}