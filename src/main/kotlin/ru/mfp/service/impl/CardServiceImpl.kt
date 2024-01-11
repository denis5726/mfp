package ru.mfp.service.impl

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import ru.mfp.dto.CardDto
import ru.mfp.dto.CreatedCardDto
import ru.mfp.entity.Card
import ru.mfp.exception.CardCreatingException
import ru.mfp.exception.IllegalServerStateException
import ru.mfp.exception.ResourceNotFoundException
import ru.mfp.mapper.CardMapper
import ru.mfp.model.JwtAuthentication
import ru.mfp.repository.CardRepository
import ru.mfp.repository.UserRepository
import ru.mfp.service.CardService
import java.util.*

private val log = KotlinLogging.logger { }

@Service
class CardServiceImpl(
    val repository: CardRepository, val mapper: CardMapper, val userRepository: UserRepository
) : CardService {

    override fun findCardById(id: UUID, authentication: JwtAuthentication): CardDto = mapper.toDto(
        repository.findByIdAndUserId(id, authentication.id) ?: throw ResourceNotFoundException("Card not found")
    )

    override fun findCards(authentication: JwtAuthentication): List<CardDto> =
        mapper.toDtoList(repository.findByUserId(authentication.id))

    override fun addCard(createdCardDto: CreatedCardDto, authentication: JwtAuthentication): CardDto {
        val currency: Currency
        try {
            currency = Currency.getInstance(createdCardDto.currency)
        } catch (e: IllegalArgumentException) {
            throw CardCreatingException(HttpStatus.BAD_REQUEST, "Invalid currency code: ${createdCardDto.currency}")
        }
        val userCards = repository.findByUserId(authentication.id)
        if (userCards.any { it.bankAccountId == createdCardDto.bankAccountId }) {
            log.error { "Attempt to add another card with bankAccountId=${createdCardDto.bankAccountId}" }
            throw CardCreatingException(
                HttpStatus.BAD_REQUEST, "Card with this bankAccountId (${createdCardDto.bankAccountId}) already exists"
            )
        }
        val user = userRepository.findById(authentication.id)
            .orElseThrow { throw IllegalServerStateException("User data not found in database!") }
        val card = Card()
        card.user = user
        card.bankAccountId = createdCardDto.bankAccountId
        card.currency = currency
        val savedCard = repository.save(card)
        log.info { "Card added for user: $savedCard" }
        return mapper.toDto(savedCard)
    }

    override fun deleteCard(id: UUID, authentication: JwtAuthentication) {
        val card = repository.findByIdAndUserId(id, authentication.id)
            ?: throw ResourceNotFoundException("Card not found")
        repository.delete(card)
        log.info { "Card deleted: $card" }
    }
}
