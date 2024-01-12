package ru.mfp.card.mapper

import org.mapstruct.Mapper
import ru.mfp.card.dto.CardDto
import ru.mfp.card.entity.Card

@Mapper
interface CardMapper {

    fun toDto(card: Card): CardDto

    fun toDtoList(card: List<Card>): List<CardDto>
}
