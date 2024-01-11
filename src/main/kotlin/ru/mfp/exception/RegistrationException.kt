package ru.mfp.exception

import org.springframework.http.HttpStatus

class RegistrationException(message: String) : MfpApiException(HttpStatus.BAD_REQUEST, message)
