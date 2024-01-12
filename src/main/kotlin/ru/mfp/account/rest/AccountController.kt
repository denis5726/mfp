package ru.mfp.account.rest

import org.springframework.web.bind.annotation.*
import ru.mfp.account.dto.AccountCreatingRequestDto
import ru.mfp.common.model.JwtAuthentication
import ru.mfp.account.service.AccountService

@RestController
@RequestMapping("/accounts")
class AccountController(
    val accountService: AccountService
) {

    @GetMapping
    fun findAccounts(authentication: JwtAuthentication) = accountService.findAccounts(authentication)

    @PostMapping
    fun addAccount(
        @RequestBody accountCreatingRequestDto: AccountCreatingRequestDto,
        authentication: JwtAuthentication
    ) = accountService.addAccount(accountCreatingRequestDto, authentication)
}
