package ru.mfp.exception

import org.springframework.http.HttpStatus

class EmailVerificationException(message: String) : MfpServerException(HttpStatus.BAD_REQUEST, message)