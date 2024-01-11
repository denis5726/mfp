package ru.mfp.rest

import org.springframework.web.bind.annotation.*
import ru.mfp.dto.CreatedAccountDto
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
        @RequestBody createdAccountDto: CreatedAccountDto,
        authentication: JwtAuthentication
    ) = accountService.addAccount(createdAccountDto, authentication)
}
