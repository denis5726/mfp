package ru.mfp.common.exception

import org.springframework.http.HttpStatus

class AuthorizationException(message: String) : MfpApiException(HttpStatus.FORBIDDEN, message)