package ru.mfp.exception

import org.springframework.http.HttpStatus

class AccountCreatingException(message: String) : MfpApiException(HttpStatus.BAD_REQUEST, message)
