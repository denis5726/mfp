package ru.mfp.email.exception

import org.springframework.http.HttpStatus
import ru.mfp.common.exception.MfpApiException

class EmailVerificationException(message: String) : MfpApiException(HttpStatus.BAD_REQUEST, message)
