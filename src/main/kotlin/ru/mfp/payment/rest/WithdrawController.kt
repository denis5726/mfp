package ru.mfp.payment.rest

import org.springframework.web.bind.annotation.*
import ru.mfp.common.config.security.aop.NotBanned
import ru.mfp.common.config.security.aop.SolvencyVerified
import ru.mfp.payment.dto.WithdrawCreatingRequestDto
import ru.mfp.common.model.JwtAuthentication
import ru.mfp.payment.service.WithdrawService

@RestController
@RequestMapping("/withdraws")
class WithdrawController(
    private val service: WithdrawService
) {

    @GetMapping
    @NotBanned
    fun findWithdraws(
        @RequestParam(required = false, defaultValue = "0") page: Int,
        authentication: JwtAuthentication
    ) = service.findWithdraws(page, authentication)

    @PostMapping
    @SolvencyVerified
    fun addWithdraw(
        @RequestBody withdrawCreatingRequestDto: WithdrawCreatingRequestDto,
        authentication: JwtAuthentication
    ) = service.addWithdraw(withdrawCreatingRequestDto, authentication)
}
