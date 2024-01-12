package ru.mfp.card.exception

import org.springframework.http.HttpStatus
import ru.mfp.common.exception.MfpApiException

class CardCreatingException(message: String) : MfpApiException(HttpStatus.BAD_REQUEST, message)
