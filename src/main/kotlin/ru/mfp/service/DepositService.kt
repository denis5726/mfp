package ru.mfp.service

import ru.mfp.dto.DepositDto
import ru.mfp.dto.DepositRequestDto
import ru.mfp.model.JwtAuthentication

interface DepositService {

    fun findDeposits(authentication: JwtAuthentication): List<DepositDto>

    fun addDeposit(depositRequestDto: DepositRequestDto, authentication: JwtAuthentication): DepositDto
}