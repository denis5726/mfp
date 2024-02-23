package ru.mfp.account.service

import java.util.UUID
import ru.mfp.account.dto.CardCreatingRequestDto
import ru.mfp.account.dto.CardDto
import ru.mfp.common.model.JwtAuthentication

interface CardService {

    fun findCardById(id: UUID, authentication: JwtAuthentication): CardDto

    fun findCards(authentication: JwtAuthentication): List<CardDto>

    fun addCard(cardCreatingRequestDto: CardCreatingRequestDto, authentication: JwtAuthentication): CardDto

    fun deleteCard(id: UUID, authentication: JwtAuthentication)
}
