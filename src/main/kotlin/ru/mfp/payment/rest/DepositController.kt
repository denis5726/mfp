package ru.mfp.payment.rest

import org.springframework.web.bind.annotation.*
import ru.mfp.common.config.security.aop.NotBanned
import ru.mfp.common.config.security.aop.SolvencyVerified
import ru.mfp.payment.dto.DepositCreatingRequestDto
import ru.mfp.common.model.JwtAuthentication
import ru.mfp.payment.service.DepositService

@RestController
@RequestMapping("/deposits")
class DepositController(
    val service: DepositService
) {

    @GetMapping
    @NotBanned
    fun findDeposits(
        @RequestParam(required = false, defaultValue = "0") page: Int,
        authentication: JwtAuthentication
    ) = service.findDeposits(page, authentication)

    @PostMapping
    @SolvencyVerified
    fun addDeposit(
        @RequestBody depositCreatingRequestDto: DepositCreatingRequestDto,
        authentication: JwtAuthentication
    ) =
        service.addDeposit(depositCreatingRequestDto, authentication)
}
