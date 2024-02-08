package ru.mfp.payment.service.impl

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import ru.mfp.account.repository.AccountRepository
import ru.mfp.account.repository.CardRepository
import ru.mfp.payment.client.PaymentApiClientService
import ru.mfp.payment.dto.PaymentCreatingRequestDto
import ru.mfp.payment.dto.PaymentEventDto
import ru.mfp.payment.exception.PaymentCreatingException
import ru.mfp.payment.exception.PaymentServiceApiException
import ru.mfp.payment.kafka.producer.PaymentProducer
import ru.mfp.payment.model.PaymentCreatingResult
import ru.mfp.payment.service.PaymentService
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

private val log = KotlinLogging.logger { }

@Component
class PaymentServiceImpl(
    private val paymentApiClientService: PaymentApiClientService,
    private val cardRepository: CardRepository,
    private val accountRepository: AccountRepository,
    private val paymentProducer: PaymentProducer,
) : PaymentService {
    @Value("\${mfp.payment-service.main-bank-account-id}")
    private var mainBankAccountId: UUID = UUID.randomUUID()

    override fun doPayment(
        userId: UUID,
        accountId: UUID,
        cardId: UUID,
        amount: BigDecimal,
        toBank: Boolean
    ): PaymentCreatingResult {
        val card = cardRepository.findById(cardId).orElseThrow {
            throw PaymentCreatingException("Card with id=$cardId is not found")
        }
        val account = accountRepository.findById(accountId).orElseThrow {
            throw PaymentCreatingException("Account with id=$accountId is not found")
        }
        if (account.currency != card.currency) {
            throw PaymentCreatingException("Account and card currencies is not equal!")
        }
        val paymentRequest = buildAbstractRequest(
            if (toBank) card.bankAccountId else mainBankAccountId,
            if (toBank) mainBankAccountId else card.bankAccountId,
            amount,
            card.currency
        )
        val paymentType = if (toBank) PaymentEventDto.PaymentType.DEPOSIT else PaymentEventDto.PaymentType.WITHDRAW
        val paymentDto = try {
            paymentApiClientService.createPayment(paymentRequest)
        } catch (e: PaymentServiceApiException) {
            paymentProducer.sendPaymentEvent(
                PaymentEventDto(
                    userId,
                    paymentRequest.id,
                    null,
                    accountId,
                    cardId,
                    false,
                    e.message,
                    amount.toString(),
                    card.currency.currencyCode,
                    paymentType
                )
            )
            throw PaymentCreatingException("Payment service error: ${e.message}")
        }
        val event = PaymentEventDto(
            userId,
            paymentRequest.id,
            paymentDto.operationId,
            accountId,
            cardId,
            paymentDto.decision,
            paymentDto.description,
            amount.toString(),
            card.currency.currencyCode,
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