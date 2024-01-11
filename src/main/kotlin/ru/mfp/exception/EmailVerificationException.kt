package ru.mfp.exception

import org.springframework.http.HttpStatus

class EmailVerificationException(message: String) : MfpApiException(HttpStatus.BAD_REQUEST, message)
