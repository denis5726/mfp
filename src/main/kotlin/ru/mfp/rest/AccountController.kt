package ru.mfp.rest

import org.springframework.web.bind.annotation.*
import ru.mfp.dto.AccountCreatingRequestDto
import ru.mfp.model.JwtAuthentication
import ru.mfp.service.AccountService

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
