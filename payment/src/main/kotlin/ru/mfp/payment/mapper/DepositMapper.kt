package ru.mfp.payment.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import ru.mfp.payment.dto.DepositDto
import ru.mfp.payment.entity.Deposit

@Mapper
interface DepositMapper {

    @Mapping(target = "accountId", source = "account.id")
    @Mapping(target = "cardId", source = "card.id")
    @Mapping(target = "currency", source = "account.currency")
    fun toDto(deposit: Deposit): DepositDto

    fun toDtoList(deposit: List<Deposit>): List<DepositDto>
}
