package ru.mfp.deposit.service.impl

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import ru.mfp.account.entity.Account
import ru.mfp.account.repository.AccountRepository
import ru.mfp.card.entity.Card
import ru.mfp.card.repository.CardRepository
import ru.mfp.common.exception.IllegalServerStateException
import ru.mfp.common.exception.ResourceNotFoundException
import ru.mfp.common.model.JwtAuthentication
import ru.mfp.deposit.dto.DepositCreatingRequestDto
import ru.mfp.deposit.dto.DepositDto
import ru.mfp.deposit.entity.Deposit
import ru.mfp.deposit.exception.DepositCreatingException
import ru.mfp.deposit.exception.PaymentApiException
import ru.mfp.deposit.kafka.producer.DepositProducer
import ru.mfp.deposit.mapper.DepositMapper
import ru.mfp.deposit.repository.DepositRepository
import ru.mfp.deposit.service.DepositService
import ru.mfp.payment.PaymentClientService
import ru.mfp.payment.dto.PaymentCreatingRequestDto
import ru.mfp.payment.dto.PaymentDto
import java.time.LocalDateTime
import java.util.*

private val log = KotlinLogging.logger { }

@Service
class DepositServiceImpl(
    private val repository: DepositRepository,
    private val mapper: DepositMapper,
    private val paymentClientService: PaymentClientService,
    private val cardRepository: CardRepository,
    private val accountRepository: AccountRepository,
    private val depositProducer: DepositProducer
) : DepositService {
    private val pageSize = 30

    @Value("\${mfp.payment.main-bank-account-id}")
    private var mainBankAccountId: UUID? = null

    override fun findDepositByPaymentId(id: UUID): DepositDto =
        mapper.toDto(repository.findByPaymentId(id) ?: throw ResourceNotFoundException("Deposit not found"))


    override fun findDeposits(page: Int, authentication: JwtAuthentication): List<DepositDto> =
        mapper.toDtoList(
            repository.findByAccountUserIdOrderByCreatedAtDesc(
                authentication.id,
                PageRequest.of(page, pageSize)
            ).content
        )

    override fun addDeposit(
        depositCreatingRequestDto: DepositCreatingRequestDto,
        authentication: JwtAuthentication
    ): DepositDto {
        val card = cardRepository.findById(depositCreatingRequestDto.cardId).orElseThrow {
            throw DepositCreatingException("Card with id=${depositCreatingRequestDto.cardId} is not found")
        }
        val account = accountRepository.findById(depositCreatingRequestDto.accountId).orElseThrow {
            throw DepositCreatingException("Account with id=${depositCreatingRequestDto.accountId} is not found")
        }
        if (account.currency != card.currency) {
            throw DepositCreatingException("Account and card currencies is not equal!")
        }
        val paymentDto = createPayment(card, depositCreatingRequestDto, account, authentication)
        val deposit = buildDeposit(paymentDto, card, account, depositCreatingRequestDto)
        account.amount += depositCreatingRequestDto.amount
        val saved = repository.save(deposit)
        depositProducer.sendDepositCreatedEvent(saved)
        return mapper.toDto(saved)
    }

    private fun createPayment(
        card: Card,
        depositCreatingRequestDto: DepositCreatingRequestDto,
        account: Account,
        authentication: JwtAuthentication
    ): PaymentDto {
        val paymentRequest = buildPaymentRequest(card, depositCreatingRequestDto, account)
        val paymentDto: PaymentDto = try {
            paymentClientService.createPayment(paymentRequest)
        } catch (e: PaymentApiException) {
            log.error { "Exception during payment service request: ${e.message}" }
            depositProducer.sendDepositErrorEvent(
                depositCreatingRequestDto,
                authentication.id,
                e.message,
                paymentRequest.id,
                account.currency
            )
            throw DepositCreatingException("Payment service error")
        }
        if (!paymentDto.decision) {
            log.error { "Payment ${paymentRequest.id} declined" }
            depositProducer.sendDepositErrorEvent(
                depositCreatingRequestDto,
                authentication.id,
                paymentDto.description,
                paymentRequest.id,
                account.currency
            )
            throw DepositCreatingException("Payment declined: ${paymentDto.description}")
        }
        return paymentDto
    }

    private fun buildDeposit(
        paymentDto: PaymentDto,
        card: Card,
        account: Account,
        depositCreatingRequestDto: DepositCreatingRequestDto
    ): Deposit {
        val deposit = mapper.fromPaymentDto(paymentDto)
        deposit.card = card
        deposit.account = account
        deposit.amount = depositCreatingRequestDto.amount
        return deposit
    }

    private fun buildPaymentRequest(
        card: Card,
        depositCreatingRequestDto: DepositCreatingRequestDto,
        account: Account
    ) =
        PaymentCreatingRequestDto(
            UUID.randomUUID(),
            card.bankAccountId,
            mainBankAccountId ?: run {
                log.error { "Main bank account id is not provided!" }
                throw IllegalServerStateException("Internal server error")
            },
            depositCreatingRequestDto.amount.toString(),
            account.currency.currencyCode,
            LocalDateTime.now()
        )
}
