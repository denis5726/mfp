package ru.mfp.payment.service.impl

import mu.KotlinLogging
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.mfp.account.repository.AccountRepository
import ru.mfp.account.repository.CardRepository
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
import java.math.BigDecimal

private val log = KotlinLogging.logger {  }

@Service
class WithdrawServiceImpl(
    private val repository: WithdrawRepository,
    private val depositRepository: DepositRepository,
    private val mapper: WithdrawMapper,
    private val cardRepository: CardRepository,
    private val accountRepository: AccountRepository,
    private val paymentService: PaymentService
) : WithdrawService {
    private val pageSize = 30
    private val requiredTotalDepositForWithdrawing = BigDecimal.valueOf(1000)

    override fun findWithdraws(page: Int, authentication: JwtAuthentication): List<WithdrawDto> =
        mapper.toDtoList(
            repository.findByAccountUserIdOrderByCreatedAtDesc(
                authentication.id,
                PageRequest.of(page, pageSize)
            ).content
        )

    @Transactional
    override fun addWithdraw(
        withdrawCreatingRequestDto: WithdrawCreatingRequestDto,
        authentication: JwtAuthentication
    ): WithdrawDto {
        val account = accountRepository.findById(withdrawCreatingRequestDto.accountId).orElseThrow {
            throw PaymentCreatingException("Account not found")
        }
        if (account.amount < withdrawCreatingRequestDto.amount) {
            throw PaymentCreatingException("You don't have enough of money")
        }
        val totalDepositAmount = depositRepository.findSumOfAllDepositAmounts(authentication.id)
        if (totalDepositAmount < requiredTotalDepositForWithdrawing) {
            throw PaymentCreatingException(
                "You need to have total deposit amount greater or equal than $requiredTotalDepositForWithdrawing"
            )
        }
        val result = paymentService.doPayment(
            authentication.id,
            withdrawCreatingRequestDto.accountId,
            withdrawCreatingRequestDto.cardId,
            withdrawCreatingRequestDto.amount,
            false
        )
        val payment = result.paymentDto
        val withdraw = Withdraw()
        withdraw.card = cardRepository.findById(withdrawCreatingRequestDto.cardId).orElseThrow()
        withdraw.account = account
        withdraw.paymentId = payment.paymentId
        withdraw.operationId = payment.operationId
        withdraw.amount = withdrawCreatingRequestDto.amount
        account.amount -= withdraw.amount
        accountRepository.save(account)
        val dto = mapper.toDto(repository.saveAndFlush(withdraw))
        result.messageCallback()
        return dto
    }
}