package ru.mfp.account.rest

import org.springframework.web.bind.annotation.*
import ru.mfp.account.dto.CardCreatingRequestDto
import ru.mfp.common.model.JwtAuthentication
import ru.mfp.account.service.CardService
import java.util.*

@RestController
@RequestMapping("/cards")
class CardController(
    val cardService: CardService
) {

    @GetMapping("/{id}")
    fun findCards(@PathVariable id: UUID, authentication: JwtAuthentication) =
        cardService.findCardById(id, authentication)

    @GetMapping
    fun findCards(authentication: JwtAuthentication) = cardService.findCards(authentication)

    @PostMapping
    fun addCard(@RequestBody cardCreatingRequestDto: CardCreatingRequestDto, authentication: JwtAuthentication) =
        cardService.addCard(cardCreatingRequestDto, authentication)

    @DeleteMapping("/{id}")
    fun deleteCard(@PathVariable id: UUID, authentication: JwtAuthentication) =
        cardService.deleteCard(id, authentication)
}
