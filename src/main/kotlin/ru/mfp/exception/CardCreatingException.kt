package ru.mfp.exception

import org.springframework.http.HttpStatus

class CardCreatingException(message: String) : MfpApiException(HttpStatus.BAD_REQUEST, message)
