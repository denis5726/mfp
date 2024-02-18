package ru.mfp.payment.service.impl

import java.math.BigDecimal
import mu.KotlinLogging
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.mfp.account.service.AccountService
import ru.mfp.common.model.JwtAuthentication
import ru.mfp.payment.dto.WithdrawCreatingRequestDto
import ru.mfp.payment.dto.WithdrawDto
import ru.mfp.payment.entity.Withdraw
import ru.mfp.payment.exception.PaymentCreatingException
import ru.mfp.payment.mapper.WithdrawMapper
import ru.mfp.payment.repository.DepositRepository
import ru.mfp.payment.repository.WithdrawRepository
import ru.mfp.payment.service.PaymentService
import ru.mfp.payment.service.WithdrawService

private val log = KotlinLogging.logger { }

@Service
class WithdrawServiceImpl(
    private val repository: WithdrawRepository,
    private val depositRepository: DepositRepository,
    private val mapper: WithdrawMapper,
    private val paymentService: PaymentService,
    private val accountService: AccountService
) : WithdrawService {
    private val pageSize = 30
    private val requiredTotalDepositForWithdrawing = BigDecimal.valueOf(1000)

    override fun findWithdraws(page: Int, authentication: JwtAuthentication): List<WithdrawDto> =
        mapper.toDtoList(
            repository.findByAccountIdInOrderByCreatedAtDesc(
                getUserAccountsIds(authentication),
                PageRequest.of(page, pageSize)
            ).content
        )


    @Transactional
    override fun addWithdraw(
        withdrawCreatingRequestDto: WithdrawCreatingRequestDto,
        authentication: JwtAuthentication
    ): WithdrawDto {
        val account = accountService.findAccounts(authentication)
            .firstOrNull { it.id == withdrawCreatingRequestDto.accountId }
            ?: throw PaymentCreatingException("Account not found")
        if (BigDecimal(account.amount) < withdrawCreatingRequestDto.amount) {
            throw PaymentCreatingException("You don't have enough of money")
        }
        val totalDepositAmount = getTotalDepositAmount(authentication)
        if (totalDepositAmount < requiredTotalDepositForWithdrawing) {
            throw PaymentCreatingException(
                "You need to have total deposit amount greater or equal than $requiredTotalDepositForWithdrawing"
            )
        }
        log.info { "Executing payment (userId=${authentication.id}), request: $withdrawCreatingRequestDto" }
        val result = paymentService.doPayment(
            authentication,
            withdrawCreatingRequestDto.accountId,
            withdrawCreatingRequestDto.cardId,
            withdrawCreatingRequestDto.amount,
            false
        )
        val payment = result.paymentDto
        val withdraw = Withdraw(
            accountId = withdrawCreatingRequestDto.accountId,
            cardId = withdrawCreatingRequestDto.cardId,
            paymentId = payment.paymentId,
            operationId = payment.operationId,
            amount = withdrawCreatingRequestDto.amount
        )
        val dto = mapper.toDto(repository.save(withdraw))
        result.messageCallback()
        return dto
    }

    private fun getUserAccountsIds(authentication: JwtAuthentication) =
        accountService.findAccounts(authentication).map { it.id }

    private fun getTotalDepositAmount(authentication: JwtAuthentication) =
        depositRepository.findSumOfAllDepositAmounts(
            getUserAccountsIds(authentication)
        )
}