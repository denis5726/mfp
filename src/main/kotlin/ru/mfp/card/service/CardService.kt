package ru.mfp.card.service

import ru.mfp.card.dto.CardDto
import ru.mfp.card.dto.CardCreatingRequestDto
import ru.mfp.common.model.JwtAuthentication
import java.util.*

interface CardService {

    fun findCardById(id: UUID, authentication: JwtAuthentication): CardDto

    fun findCards(authentication: JwtAuthentication): List<CardDto>

    fun addCard(cardCreatingRequestDto: CardCreatingRequestDto, authentication: JwtAuthentication): CardDto

    fun deleteCard(id: UUID, authentication: JwtAuthentication)
}
