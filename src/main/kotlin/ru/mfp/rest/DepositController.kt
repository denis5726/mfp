package ru.mfp.rest

import org.springframework.web.bind.annotation.*
import ru.mfp.dto.DepositCreatingRequestDto
import ru.mfp.model.JwtAuthentication
import ru.mfp.service.DepositService

@RestController
@RequestMapping("/deposits")
class DepositController(
    val service: DepositService
) {

    @GetMapping
    fun findDeposits(@RequestParam(required = false, defaultValue = "0") page: Int, authentication: JwtAuthentication) =
        service.findDeposits(page, authentication)

    @PostMapping
    fun addDeposit(
        @RequestBody depositCreatingRequestDto: DepositCreatingRequestDto,
        authentication: JwtAuthentication
    ) =
        service.addDeposit(depositCreatingRequestDto, authentication)
}
