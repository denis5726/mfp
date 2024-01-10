package ru.mfp.exception

import org.springframework.http.HttpStatus

class ResourceNotFoundException(message: String) : MfpServerException(HttpStatus.NOT_FOUND, message)