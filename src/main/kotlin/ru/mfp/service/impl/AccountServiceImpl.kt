package ru.mfp.service.impl

import org.springframework.stereotype.Service
import ru.mfp.dto.AccountDto
import ru.mfp.dto.CreatedAccountDto
import ru.mfp.mapper.AccountMapper
import ru.mfp.model.JwtAuthentication
import ru.mfp.repository.AccountRepository
import ru.mfp.service.AccountService

@Service
class AccountServiceImpl(
    private val accountRepository: AccountRepository,
    private val accountMapper: AccountMapper
) : AccountService {

    override fun findAccounts(authentication: JwtAuthentication) = accountMapper.toDtoList(accountRepository.findByUser(authentication.id))

    override fun addAccount(createdAccountDto: CreatedAccountDto, authentication: JwtAuthentication): AccountDto {
        TODO("Not yet implemented")
    }
}
