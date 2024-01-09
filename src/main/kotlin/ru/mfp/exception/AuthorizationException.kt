package ru.mfp.exception

import org.springframework.http.HttpStatus

class AuthorizationException(status: HttpStatus, message: String) : MfpServerException(status, message)
