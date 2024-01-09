package ru.mfp.exception

import org.springframework.http.HttpStatus

open class MfpServerException(val status: HttpStatus, message: String) : RuntimeException(message)
