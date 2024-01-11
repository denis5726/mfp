package ru.mfp.service.impl

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.mfp.dto.AccountDto
import ru.mfp.dto.CreatedAccountDto
import ru.mfp.entity.Account
import ru.mfp.exception.AccountCreatingException
import ru.mfp.exception.IllegalServerStateException
import ru.mfp.mapper.AccountMapper
import ru.mfp.model.JwtAuthentication
import ru.mfp.repository.AccountRepository
import ru.mfp.repository.UserRepository
import ru.mfp.service.AccountService
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
        accountMapper.toDtoList(accountRepository.findByUserId(authentication.id))

    @Transactional
    override fun addAccount(createdAccountDto: CreatedAccountDto, authentication: JwtAuthentication): AccountDto {
        val userAccounts = accountRepository.findByUserId(authentication.id)
        if (userAccounts.isNotEmpty()) {
            log.error { "Attempt to create another account by user (id=${authentication.id})" }
            throw AccountCreatingException(HttpStatus.BAD_REQUEST, "You already have an account!")
        }
        val optionalUser = userRepository.findById(authentication.id)
        if (optionalUser.isEmpty) {
            log.error { "User doesn't exist, but token was created, id=${authentication.id}" }
            throw IllegalServerStateException("User data not found in database")
        }

        val account = Account()
        account.user = optionalUser.get()
        account.amount = BigDecimal.valueOf(0L)
        account.currency = convertCurrency(createdAccountDto)
        return accountMapper.toDto(accountRepository.save(account))
    }

    private fun convertCurrency(createdAccountDto: CreatedAccountDto): Currency? {
        try {
            return Currency.getInstance(createdAccountDto.currency)
        } catch (e: IllegalArgumentException) {
            log.error { "Provided invalid currency code: ${createdAccountDto.currency}" }
            throw AccountCreatingException(
                HttpStatus.BAD_REQUEST,
                "Invalid currency code: ${createdAccountDto.currency}"
            )
        }
    }
}
