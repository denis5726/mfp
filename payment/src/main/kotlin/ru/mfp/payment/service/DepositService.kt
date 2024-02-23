package ru.mfp.payment.service

import java.util.UUID
import ru.mfp.common.model.JwtAuthentication
import ru.mfp.payment.dto.DepositCreatingRequestDto
import ru.mfp.payment.dto.DepositDto

interface DepositService {

    fun findDepositByPaymentId(id: UUID): DepositDto

    fun findDeposits(page: Int, authentication: JwtAuthentication): List<DepositDto>

    fun addDeposit(depositCreatingRequestDto: DepositCreatingRequestDto, authentication: JwtAuthentication): DepositDto
}
