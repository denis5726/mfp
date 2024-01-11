package ru.mfp.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import ru.mfp.dto.DepositDto
import ru.mfp.dto.PaymentDto
import ru.mfp.entity.Deposit

@Mapper
interface DepositMapper {

    @Mapping(target = "accountId", source = "account.id")
    @Mapping(target = "cardId", source = "card.id")
    fun toDto(deposit: Deposit): DepositDto

    fun toDtoList(deposit: List<Deposit>): List<DepositDto>

    fun fromPaymentDto(paymentDto: PaymentDto): Deposit
}
