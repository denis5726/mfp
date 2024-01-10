package ru.mfp.service.impl

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import ru.mfp.client.PaymentClientService
import ru.mfp.dto.CreatedPaymentDto
import ru.mfp.dto.DepositDto
import ru.mfp.dto.DepositRequestDto
import ru.mfp.exception.DepositCreatingException
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
    @Value("mfp.payment.main-bank-account-id")
    private var mainBankAccountId: UUID? = null
    private val mainBankAccountExceptionSupplier: () -> UUID = {
        log.error { "Main bank account id is not provided!" }
        throw DepositCreatingException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error")
    }

    override fun findDeposits(authentication: JwtAuthentication): List<DepositDto> =
        mapper.toDtoList(repository.findByAccountUserId(authentication.id))

    override fun addDeposit(depositRequestDto: DepositRequestDto, authentication: JwtAuthentication): DepositDto {
        val card = cardRepository.findById(
            depositRequestDto.cardId ?: throw DepositCreatingException(
                HttpStatus.BAD_REQUEST,
                "Card id is not provided"
            )
        ).orElseGet {
            throw DepositCreatingException(
                HttpStatus.BAD_REQUEST,
                "Card with id=${depositRequestDto.cardId} is not found"
            )
        }
        val cardAccountId = card.accountId ?: throw DepositCreatingException(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Card account id not found in database!"
        )
        val account = accountRepository.findById(
            depositRequestDto.accountId ?: throw DepositCreatingException(
                HttpStatus.BAD_REQUEST,
                "Account id is not provided"
            )
        ).orElseThrow {
            throw DepositCreatingException(
                HttpStatus.BAD_REQUEST,
                "Account with id=${depositRequestDto.accountId} is not found"
            )
        }
        if (depositRequestDto.amount == null) {
            throw DepositCreatingException(
                HttpStatus.BAD_REQUEST,
                "Amount is not provided"
            )
        }
        // TODO add card currency
        val createdPaymentDto = CreatedPaymentDto(
            UUID.randomUUID(),
            cardAccountId,
            mainBankAccountId ?: mainBankAccountExceptionSupplier.invoke(),
            depositRequestDto.amount.toString(),
            "RUB",
            LocalDateTime.now()
        )
        val paymentDto = paymentClientService.createPayment(createdPaymentDto)
        val deposit = mapper.fromDto(paymentDto)
        deposit.card = card
        deposit.account = account
        if (paymentDto.decision && account.amount != null) {
            account.amount = account.amount?.plus(depositRequestDto.amount)
        }
        return mapper.toDto(repository.save(deposit))
    }
}