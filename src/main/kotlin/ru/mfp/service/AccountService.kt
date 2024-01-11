package ru.mfp.service

import ru.mfp.dto.AccountDto
import ru.mfp.dto.AccountCreatingRequestDto
import ru.mfp.model.JwtAuthentication

interface AccountService {

    fun findAccounts(authentication: JwtAuthentication): List<AccountDto>

    fun addAccount(accountCreatingRequestDto: AccountCreatingRequestDto, authentication: JwtAuthentication): AccountDto
}
