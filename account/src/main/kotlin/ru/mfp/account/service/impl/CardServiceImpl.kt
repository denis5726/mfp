package ru.mfp.account.service.impl

import java.util.Currency
import java.util.UUID
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.mfp.account.dto.CardCreatingRequestDto
import ru.mfp.account.dto.CardDto
import ru.mfp.account.entity.Card
import ru.mfp.account.exception.CardCreatingException
import ru.mfp.account.mapper.CardMapper
import ru.mfp.account.repository.CardRepository
import ru.mfp.account.service.CardService
import ru.mfp.common.exception.ResourceNotFoundException
import ru.mfp.common.model.JwtAuthentication

private val log = KotlinLogging.logger { }

@Service
class CardServiceImpl(
    private val repository: CardRepository,
    private val mapper: CardMapper
) : CardService {

    override fun findCardById(id: UUID, authentication: JwtAuthentication): CardDto = mapper.toDto(
        repository.findByIdAndUserId(id, authentication.id) ?: throw ResourceNotFoundException("Card not found")
    )

    override fun findCards(authentication: JwtAuthentication): List<CardDto> =
        mapper.toDtoList(repository.findByUserIdOrderByCreatedAtDesc(authentication.id))

    @Transactional
    override fun addCard(cardCreatingRequestDto: CardCreatingRequestDto, authentication: JwtAuthentication): CardDto {
        val currency = try {
            Currency.getInstance(cardCreatingRequestDto.currency)
        } catch (e: IllegalArgumentException) {
            throw CardCreatingException("Invalid currency code: ${cardCreatingRequestDto.currency}")
        }
        val userCards = repository.findByUserIdOrderByCreatedAtDesc(authentication.id)
        if (userCards.any { it.bankAccountId == cardCreatingRequestDto.bankAccountId }) {
            log.error { "Attempt to add another card with bankAccountId=${cardCreatingRequestDto.bankAccountId}" }
            throw CardCreatingException("Card with this bankAccountId (${cardCreatingRequestDto.bankAccountId}) already exists")
        }
        val savedCard = repository.save(
            Card(
                userId = authentication.id,
                bankAccountId = cardCreatingRequestDto.bankAccountId,
                currency = currency
            )
        )
        log.info { "Card added for user: $savedCard" }
        return mapper.toDto(savedCard)
    }

    @Transactional
    override fun deleteCard(id: UUID, authentication: JwtAuthentication) {
        val card = repository.findByIdAndUserId(id, authentication.id)
            ?: throw ResourceNotFoundException("Card not found")
        repository.delete(card)
        log.info { "Card deleted: $card" }
    }
}
