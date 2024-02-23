package ru.mfp.account.rest

import java.util.*
import org.springframework.web.bind.annotation.*
import ru.mfp.account.dto.CardCreatingRequestDto
import ru.mfp.account.service.CardService
import ru.mfp.common.config.security.aop.EmailVerifiedOrHigher
import ru.mfp.common.config.security.aop.NotBanned
import ru.mfp.common.model.JwtAuthentication

@RestController
@RequestMapping("/cards")
class CardController(
    private val cardService: CardService
) {

    @GetMapping("/{id}")
    @NotBanned
    fun findCards(@PathVariable id: UUID, authentication: JwtAuthentication) =
        cardService.findCardById(id, authentication)

    @GetMapping
    @NotBanned
    fun findCards(authentication: JwtAuthentication) = cardService.findCards(authentication)

    @PostMapping
    @EmailVerifiedOrHigher
    fun addCard(@RequestBody cardCreatingRequestDto: CardCreatingRequestDto, authentication: JwtAuthentication) =
        cardService.addCard(cardCreatingRequestDto, authentication)

    @DeleteMapping("/{id}")
    @EmailVerifiedOrHigher
    fun deleteCard(@PathVariable id: UUID, authentication: JwtAuthentication) =
        cardService.deleteCard(id, authentication)
}
