package ru.mfp.payment.service

import ru.mfp.payment.dto.DepositDto
import ru.mfp.payment.dto.DepositCreatingRequestDto
import ru.mfp.common.model.JwtAuthentication
import java.util.*

interface DepositService {

    fun findDepositByPaymentId(id: UUID): DepositDto

    fun findDeposits(page: Int, authentication: JwtAuthentication): List<DepositDto>

    fun addDeposit(depositCreatingRequestDto: DepositCreatingRequestDto, authentication: JwtAuthentication): DepositDto
}
