package ru.mfp.exception

import org.springframework.http.HttpStatus

class IllegalServerStateException(message: String): MfpServerException(HttpStatus.INTERNAL_SERVER_ERROR, message)