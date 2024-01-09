package ru.mfp.service

import ru.mfp.dto.AccountDto
import ru.mfp.dto.CreatedAccountDto
import ru.mfp.model.JwtAuthentication

interface AccountService {

    fun findAccounts(authentication: JwtAuthentication): List<AccountDto>

    fun addAccount(createdAccountDto: CreatedAccountDto, authentication: JwtAuthentication): AccountDto
}
