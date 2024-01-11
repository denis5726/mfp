package ru.mfp.exception

import org.springframework.http.HttpStatus

class DepositCreatingException(message: String) : MfpApiException(HttpStatus.BAD_REQUEST, message)
