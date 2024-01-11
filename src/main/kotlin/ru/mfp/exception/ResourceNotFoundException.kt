package ru.mfp.exception

import org.springframework.http.HttpStatus

class ResourceNotFoundException(message: String) : MfpApiException(HttpStatus.NOT_FOUND, message)
