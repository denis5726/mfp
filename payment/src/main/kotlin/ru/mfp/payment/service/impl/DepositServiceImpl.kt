package ru.mfp.payment.service.impl

import mu.KotlinLogging
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.mfp.account.repository.AccountRepository
import ru.mfp.account.repository.CardRepository
import ru.mfp.common.exception.ResourceNotFoundException
import ru.mfp.common.model.JwtAuthentication
import ru.mfp.payment.dto.DepositCreatingRequestDto
import ru.mfp.payment.dto.DepositDto
import ru.mfp.payment.entity.Deposit
import ru.mfp.payment.mapper.DepositMapper
import ru.mfp.payment.repository.DepositRepository
import ru.mfp.payment.service.DepositService
import ru.mfp.payment.service.PaymentService
import java.util.*

private val log = KotlinLogging.logger { }

@Service
class DepositServiceImpl(
    private val repository: DepositRepository,
    private val mapper: DepositMapper,
    private val paymentService: PaymentService,
    private val cardRepository: CardRepository,
    private val accountRepository: AccountRepository
) : DepositService {
    private val pageSize = 30

    override fun findDepositByPaymentId(id: UUID): DepositDto =
        mapper.toDto(repository.findByPaymentId(id) ?: throw ResourceNotFoundException("Deposit not found"))


    override fun findDeposits(page: Int, authentication: JwtAuthentication): List<DepositDto> =
        mapper.toDtoList(
            repository.findByAccountUserIdOrderByCreatedAtDesc(
                authentication.id,
                PageRequest.of(page, pageSize)
            ).content
        )

    @Transactional
    override fun addDeposit(
        depositCreatingRequestDto: DepositCreatingRequestDto,
        authentication: JwtAuthentication
    ): DepositDto {
        val result = paymentService.doPayment(
            authentication.id,
            depositCreatingRequestDto.accountId,
            depositCreatingRequestDto.cardId,
            depositCreatingRequestDto.amount,
            true
        )
        val payment = result.paymentDto
        val account = accountRepository.findById(depositCreatingRequestDto.accountId).orElseThrow()
        val deposit = Deposit()
        deposit.card = cardRepository.findById(depositCreatingRequestDto.cardId).orElseThrow()
        deposit.account = account
        deposit.paymentId = payment.paymentId
        deposit.operationId = payment.operationId
        deposit.amount = depositCreatingRequestDto.amount
        account.amount += deposit.amount
        accountRepository.save(account)
        val dto = mapper.toDto(repository.saveAndFlush(deposit))
        result.messageCallback()
        return dto
    }
}
