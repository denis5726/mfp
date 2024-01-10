package ru.mfp.exception

import org.springframework.http.HttpStatus

class CardCreatingException(status: HttpStatus, message: String) : MfpServerException(status, message)