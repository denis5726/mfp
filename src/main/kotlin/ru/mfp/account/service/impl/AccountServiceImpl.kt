package ru.mfp.account.service.impl

import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.mfp.account.dto.AccountCreatingRequestDto
import ru.mfp.account.dto.AccountDto
import ru.mfp.account.entity.Account
import ru.mfp.account.entity.AccountChangeReason
import ru.mfp.account.exception.AccountCreatingException
import ru.mfp.account.mapper.AccountMapper
import ru.mfp.account.repository.AccountRepository
import ru.mfp.account.service.AccountHistoryService
import ru.mfp.account.service.AccountService
import ru.mfp.auth.repository.UserRepository
import ru.mfp.common.exception.IllegalServerStateException
import ru.mfp.common.model.JwtAuthentication
import java.math.BigDecimal
import java.util.*

private val log = KotlinLogging.logger { }

@Service
class AccountServiceImpl(
    private val accountRepository: AccountRepository,
    private val accountMapper: AccountMapper,
    private val userRepository: UserRepository,
    private val accountHistoryService: AccountHistoryService
) : AccountService {
    private val creationGift = BigDecimal.valueOf(1000)

    override fun findAccounts(authentication: JwtAuthentication) =
        accountMapper.toDtoList(accountRepository.findByUserIdOrderByCreatedAtDesc(authentication.id))

    @Transactional
    override fun addAccount(
        accountCreatingRequestDto: AccountCreatingRequestDto,
        authentication: JwtAuthentication
    ): AccountDto {
        val userAccounts = accountRepository.findByUserIdOrderByCreatedAtDesc(authentication.id)
        // Пока что у пользователя может быть только один счёт
        if (userAccounts.isNotEmpty()) {
            log.error { "Attempt to create another account by user (id=${authentication.id})" }
            throw AccountCreatingException("You already have an account!")
        }
        val optionalUser = userRepository.findById(authentication.id)
        if (optionalUser.isEmpty) {
            log.error { "User doesn't exist, but token was created, id=${authentication.id}" }
            throw IllegalServerStateException("User data not found in database")
        }
        val account = Account()
        account.user = optionalUser.get()
        // Когда будет несколько счетов, подарок начислять надо только на первый
        account.amount = creationGift
        account.currency = convertCurrency(accountCreatingRequestDto)

        val saved = accountRepository.saveAndFlush(account)
        accountHistoryService.registerChange(account.id, account.amount, AccountChangeReason.CREATION_GIFT)
        return accountMapper.toDto(saved)
    }

    private fun convertCurrency(accountCreatingRequestDto: AccountCreatingRequestDto) =
        try {
            Currency.getInstance(accountCreatingRequestDto.currency)
        } catch (e: IllegalArgumentException) {
            log.error { "Provided invalid currency code: ${accountCreatingRequestDto.currency}" }
            throw AccountCreatingException("Invalid currency code: ${accountCreatingRequestDto.currency}")
        }
}
