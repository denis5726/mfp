package ru.mfp.deposit.service

import ru.mfp.deposit.dto.DepositDto
import ru.mfp.deposit.dto.DepositCreatingRequestDto
import ru.mfp.common.model.JwtAuthentication

interface DepositService {

    fun findDeposits(page: Int, authentication: JwtAuthentication): List<DepositDto>

    fun addDeposit(depositCreatingRequestDto: DepositCreatingRequestDto, authentication: JwtAuthentication): DepositDto
}
