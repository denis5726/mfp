package ru.mfp.service.impl

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import ru.mfp.client.PaymentClientService
import ru.mfp.dto.DepositCreatingRequestDto
import ru.mfp.dto.DepositDto
import ru.mfp.dto.PaymentCreatingRequestDto
import ru.mfp.exception.DepositCreatingException
import ru.mfp.exception.IllegalServerStateException
import ru.mfp.mapper.DepositMapper
import ru.mfp.model.JwtAuthentication
import ru.mfp.repository.AccountRepository
import ru.mfp.repository.CardRepository
import ru.mfp.repository.DepositRepository
import ru.mfp.service.DepositService
import java.time.LocalDateTime
import java.util.*

private val log = KotlinLogging.logger { }

@Service
class DepositServiceImpl(
    private val repository: DepositRepository,
    private val mapper: DepositMapper,
    private val paymentClientService: PaymentClientService,
    private val cardRepository: CardRepository,
    private val accountRepository: AccountRepository
) : DepositService {
    private val pageSize = 30

    @Value("\${mfp.payment.main-bank-account-id}")
    private var mainBankAccountId: UUID? = null

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
        val paymentCreatingRequestDto = PaymentCreatingRequestDto(
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
        val paymentDto = paymentClientService.createPayment(paymentCreatingRequestDto)
        val deposit = mapper.fromPaymentDto(paymentDto)
        deposit.card = card
        deposit.account = account
        deposit.amount = depositCreatingRequestDto.amount
        if (paymentDto.decision) {
            account.amount += depositCreatingRequestDto.amount
        }
        return mapper.toDto(repository.save(deposit))
    }
}
