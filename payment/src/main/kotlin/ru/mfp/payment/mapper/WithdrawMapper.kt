package ru.mfp.payment.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import ru.mfp.payment.dto.PaymentDto
import ru.mfp.payment.dto.WithdrawDto
import ru.mfp.payment.entity.Withdraw

@Mapper
interface WithdrawMapper {

    @Mapping(target = "accountId", source = "accountId.id")
    @Mapping(target = "cardId", source = "cardId.id")
    @Mapping(target = "currency", source = "accountId.currency")
    fun toDto(deposit: Withdraw): WithdrawDto

    fun toDtoList(deposit: List<Withdraw>): List<WithdrawDto>

    fun fromPaymentDto(paymentDto: PaymentDto): Withdraw
}
