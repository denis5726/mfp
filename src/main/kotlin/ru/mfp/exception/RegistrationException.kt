package ru.mfp.exception

import org.springframework.http.HttpStatus

class RegistrationException(status: HttpStatus, message: String) : MfpServerException(status, message)
