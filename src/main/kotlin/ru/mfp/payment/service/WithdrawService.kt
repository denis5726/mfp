package ru.mfp.payment.service

import ru.mfp.common.model.JwtAuthentication
import ru.mfp.payment.dto.WithdrawCreatingRequestDto
import ru.mfp.payment.dto.WithdrawDto

interface WithdrawService {

    fun findWithdraws(page: Int, authentication: JwtAuthentication): List<WithdrawDto>

    fun addWithdraw(
        withdrawCreatingRequestDto: WithdrawCreatingRequestDto,
        authentication: JwtAuthentication
    ): WithdrawDto
}