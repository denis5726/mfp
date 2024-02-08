package ru.mfp.account.mapper

import org.mapstruct.Mapper
import ru.mfp.account.dto.CardDto
import ru.mfp.account.entity.Card

@Mapper
interface CardMapper {

    fun toDto(card: Card): CardDto

    fun toDtoList(card: List<Card>): List<CardDto>
}
