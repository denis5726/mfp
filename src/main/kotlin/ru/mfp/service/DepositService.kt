package ru.mfp.service

import ru.mfp.dto.DepositDto
import ru.mfp.dto.DepositCreatingRequestDto
import ru.mfp.model.JwtAuthentication

interface DepositService {

    fun findDeposits(page: Int, authentication: JwtAuthentication): List<DepositDto>

    fun addDeposit(depositCreatingRequestDto: DepositCreatingRequestDto, authentication: JwtAuthentication): DepositDto
}
