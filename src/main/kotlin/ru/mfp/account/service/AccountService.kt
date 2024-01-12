package ru.mfp.account.service

import ru.mfp.account.dto.AccountDto
import ru.mfp.account.dto.AccountCreatingRequestDto
import ru.mfp.common.model.JwtAuthentication

interface AccountService {

    fun findAccounts(authentication: JwtAuthentication): List<AccountDto>

    fun addAccount(accountCreatingRequestDto: AccountCreatingRequestDto, authentication: JwtAuthentication): AccountDto
}
