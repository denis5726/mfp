package ru.mfp.payment.mapper

import org.mapstruct.Mapper
import ru.mfp.payment.dto.WithdrawDto
import ru.mfp.payment.entity.Withdraw

@Mapper
interface WithdrawMapper {

    fun toDto(deposit: Withdraw): WithdrawDto

    fun toDtoList(deposit: List<Withdraw>): List<WithdrawDto>
}
