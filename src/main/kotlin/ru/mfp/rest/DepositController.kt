package ru.mfp.rest

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.mfp.dto.DepositRequestDto
import ru.mfp.model.JwtAuthentication
import ru.mfp.service.DepositService

@RestController
@RequestMapping("/deposits")
class DepositController(
    val service: DepositService
) {

    @GetMapping
    fun findDeposits(authentication: JwtAuthentication) = service.findDeposits(authentication)

    @PostMapping
    fun addDeposit(depositRequestDto: DepositRequestDto, authentication: JwtAuthentication) =
        service.addDeposit(depositRequestDto, authentication)
}
