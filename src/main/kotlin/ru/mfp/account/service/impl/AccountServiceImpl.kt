package ru.mfp.account.service.impl

import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.mfp.account.dto.AccountDto
import ru.mfp.account.dto.AccountCreatingRequestDto
import ru.mfp.account.entity.Account
import ru.mfp.account.exception.AccountCreatingException
import ru.mfp.common.exception.IllegalServerStateException
import ru.mfp.account.mapper.AccountMapper
import ru.mfp.common.model.JwtAuthentication
import ru.mfp.account.repository.AccountRepository
import ru.mfp.auth.repository.UserRepository
import ru.mfp.account.service.AccountService
import java.math.BigDecimal
import java.util.*

private val log = KotlinLogging.logger { }

@Service
class AccountServiceImpl(
    private val accountRepository: AccountRepository,
    private val accountMapper: AccountMapper,
    private val userRepository: UserRepository
) : AccountService {

    override fun findAccounts(authentication: JwtAuthentication) =
        accountMapper.toDtoList(accountRepository.findByUserIdOrderByCreatedAtDesc(authentication.id))

    @Transactional
    override fun addAccount(accountCreatingRequestDto: AccountCreatingRequestDto, authentication: JwtAuthentication): AccountDto {
        val userAccounts = accountRepository.findByUserIdOrderByCreatedAtDesc(authentication.id)
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
        account.amount = BigDecimal.valueOf(0L)
        account.currency = convertCurrency(accountCreatingRequestDto)
        return accountMapper.toDto(accountRepository.save(account))
    }

    private fun convertCurrency(accountCreatingRequestDto: AccountCreatingRequestDto): Currency {
        try {
            return Currency.getInstance(accountCreatingRequestDto.currency)
        } catch (e: IllegalArgumentException) {
            log.error { "Provided invalid currency code: ${accountCreatingRequestDto.currency}" }
            throw AccountCreatingException("Invalid currency code: ${accountCreatingRequestDto.currency}")
        }
    }
}
