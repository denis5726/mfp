package ru.mfp.common.exception

import org.springframework.http.HttpStatus

open class MfpApiException(val status: HttpStatus, message: String) : MfpException(message)
