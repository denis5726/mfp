package ru.mfp.common.exception

import org.springframework.http.HttpStatus

class DtoValidationException(message: String) : MfpApiException(HttpStatus.BAD_REQUEST, message)