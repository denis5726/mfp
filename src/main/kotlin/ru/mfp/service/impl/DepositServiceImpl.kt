package ru.mfp.service.impl

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.mfp.client.PaymentClientService
import ru.mfp.dto.CreatedPaymentDto
import ru.mfp.dto.DepositDto
import ru.mfp.dto.DepositRequestDto
import ru.mfp.exception.DepositCreatingException
import ru.mfp.exception.IllegalApiStateException
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
    @Value("\${mfp.payment.main-bank-account-id}")
    private var mainBankAccountId: UUID? = null
    private val mainBankAccountExceptionSupplier: () -> UUID = {
        log.error { "Main bank account id is not provided!" }
        throw IllegalApiStateException("Internal server error")
    }

    override fun findDeposits(authentication: JwtAuthentication): List<DepositDto> =
        mapper.toDtoList(repository.findByAccountUserId(authentication.id))

    override fun addDeposit(depositRequestDto: DepositRequestDto, authentication: JwtAuthentication): DepositDto {
        val card = cardRepository.findById(depositRequestDto.cardId).orElseGet {
            throw DepositCreatingException("Card with id=${depositRequestDto.cardId} is not found")
        }
        val account = accountRepository.findById(depositRequestDto.accountId).orElseThrow {
            throw DepositCreatingException("Account with id=${depositRequestDto.accountId} is not found")
        }
        if (account.currency != card.currency) {
            throw DepositCreatingException("Account and card currencies is not equal!")
        }
        val createdPaymentDto = CreatedPaymentDto(
            UUID.randomUUID(),
            card.bankAccountId,
            mainBankAccountId ?: mainBankAccountExceptionSupplier.invoke(),
            depositRequestDto.amount.toString(),
            account.currency.currencyCode,
            LocalDateTime.now()
        )
        val paymentDto = paymentClientService.createPayment(createdPaymentDto)
        val deposit = mapper.fromDto(paymentDto)
        deposit.card = card
        deposit.account = account
        if (paymentDto.decision) {
            account.amount += depositRequestDto.amount
        }
        return mapper.toDto(repository.save(deposit))
    }
}
