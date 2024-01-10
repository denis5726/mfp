package ru.mfp.rest

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.mfp.dto.CreatedCardDto
import ru.mfp.model.JwtAuthentication
import ru.mfp.service.CardService
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
    fun addCard(@RequestBody createdCardDto: CreatedCardDto, authentication: JwtAuthentication) =
        cardService.addCard(createdCardDto, authentication)

    @DeleteMapping("/{id}")
    fun deleteCard(@PathVariable id: UUID, authentication: JwtAuthentication) =
        cardService.deleteCard(id, authentication)
}
