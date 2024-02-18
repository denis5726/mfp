package ru.mfp.payment.service.impl

import java.util.UUID
import mu.KotlinLogging
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.mfp.account.service.AccountService
import ru.mfp.common.exception.ResourceNotFoundException
import ru.mfp.common.model.JwtAuthentication
import ru.mfp.payment.dto.DepositCreatingRequestDto
import ru.mfp.payment.dto.DepositDto
import ru.mfp.payment.entity.Deposit
import ru.mfp.payment.mapper.DepositMapper
import ru.mfp.payment.repository.DepositRepository
import ru.mfp.payment.service.DepositService
import ru.mfp.payment.service.PaymentService

private val log = KotlinLogging.logger { }

@Service
class DepositServiceImpl(
    private val repository: DepositRepository,
    private val mapper: DepositMapper,
    private val paymentService: PaymentService,
    private val accountService: AccountService
) : DepositService {
    private val pageSize = 30

    override fun findDepositByPaymentId(id: UUID): DepositDto =
        mapper.toDto(repository.findByPaymentId(id) ?: throw ResourceNotFoundException("Deposit not found"))


    override fun findDeposits(page: Int, authentication: JwtAuthentication): List<DepositDto> {
        val accounts = accountService.findAccounts(authentication)
        return mapper.toDtoList(
            repository.findByAccountIdInOrderByCreatedAtDesc(
                accounts.map { it.id },
                PageRequest.of(page, pageSize)
            ).content
        )
    }


    @Transactional
    override fun addDeposit(
        depositCreatingRequestDto: DepositCreatingRequestDto,
        authentication: JwtAuthentication
    ): DepositDto {
        val result = paymentService.doPayment(
            authentication,
            depositCreatingRequestDto.accountId,
            depositCreatingRequestDto.cardId,
            depositCreatingRequestDto.amount,
            true
        )
        val payment = result.paymentDto
        val deposit = Deposit(
            accountId = depositCreatingRequestDto.accountId,
            cardId = depositCreatingRequestDto.cardId,
            paymentId = payment.paymentId,
            operationId = payment.operationId,
            amount = depositCreatingRequestDto.amount
        )

        val dto = mapper.toDto(repository.save(deposit))
        result.messageCallback()
        return dto
    }
}
