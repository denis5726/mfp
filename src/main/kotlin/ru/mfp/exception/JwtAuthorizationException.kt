package ru.mfp.exception

import org.springframework.http.HttpStatus

class JwtAuthorizationException(status: HttpStatus, message: String) : MfpServerException(status, message)
