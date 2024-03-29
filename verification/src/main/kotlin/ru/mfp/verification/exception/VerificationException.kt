package ru.mfp.verification.exception

import org.springframework.http.HttpStatus
import ru.mfp.common.exception.MfpApiException

class VerificationException(message: String) : MfpApiException(HttpStatus.BAD_REQUEST, message)
