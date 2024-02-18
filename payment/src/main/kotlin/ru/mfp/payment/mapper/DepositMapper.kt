package ru.mfp.payment.mapper

import org.mapstruct.Mapper
import ru.mfp.payment.dto.DepositDto
import ru.mfp.payment.entity.Deposit

@Mapper
interface DepositMapper {

    fun toDto(deposit: Deposit): DepositDto

    fun toDtoList(deposit: List<Deposit>): List<DepositDto>
}
