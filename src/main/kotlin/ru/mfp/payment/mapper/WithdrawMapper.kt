package ru.mfp.payment.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import ru.mfp.payment.dto.PaymentDto
import ru.mfp.payment.dto.WithdrawDto
import ru.mfp.payment.entity.Withdraw

@Mapper
interface WithdrawMapper {

    @Mapping(target = "accountId", source = "account.id")
    @Mapping(target = "cardId", source = "card.id")
    @Mapping(target = "currency", source = "account.currency")
    fun toDto(deposit: Withdraw): WithdrawDto

    fun toDtoList(deposit: List<Withdraw>): List<WithdrawDto>

    fun fromPaymentDto(paymentDto: PaymentDto): Withdraw
}
