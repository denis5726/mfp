package ru.mfp.deposit.service

import ru.mfp.deposit.dto.DepositDto
import ru.mfp.deposit.dto.DepositCreatingRequestDto
import ru.mfp.common.model.JwtAuthentication
import java.util.*

interface DepositService {

    fun findDepositByPaymentId(id: UUID): DepositDto

    fun findDeposits(page: Int, authentication: JwtAuthentication): List<DepositDto>

    fun addDeposit(depositCreatingRequestDto: DepositCreatingRequestDto, authentication: JwtAuthentication): DepositDto
}
