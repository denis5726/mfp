package ru.mfp.common.exception

import org.springframework.http.HttpStatus

class IllegalServerStateException(message: String) : MfpApiException(HttpStatus.INTERNAL_SERVER_ERROR, message)
