package ru.mfp.mapper

import org.mapstruct.Mapper
import ru.mfp.dto.CardDto
import ru.mfp.entity.Card

@Mapper
interface CardMapper {

    fun toDto(card: Card): CardDto

    fun toDtoList(card: List<Card>): List<CardDto>
}
