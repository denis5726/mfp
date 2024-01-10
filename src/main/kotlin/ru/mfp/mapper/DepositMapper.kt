package ru.mfp.mapper

import org.mapstruct.Mapper
import ru.mfp.dto.DepositDto
import ru.mfp.dto.PaymentDto
import ru.mfp.entity.Deposit

@Mapper
interface DepositMapper {

    fun toDto(deposit: Deposit): DepositDto

    fun toDtoList(deposit: List<Deposit>): List<DepositDto>

    fun fromDto(paymentDto: PaymentDto): Deposit
}
