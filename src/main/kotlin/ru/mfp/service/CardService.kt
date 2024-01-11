package ru.mfp.service

import ru.mfp.dto.CardDto
import ru.mfp.dto.CardCreatingRequestDto
import ru.mfp.model.JwtAuthentication
import java.util.*

interface CardService {

    fun findCardById(id: UUID, authentication: JwtAuthentication): CardDto

    fun findCards(authentication: JwtAuthentication): List<CardDto>

    fun addCard(cardCreatingRequestDto: CardCreatingRequestDto, authentication: JwtAuthentication): CardDto

    fun deleteCard(id: UUID, authentication: JwtAuthentication)
}
