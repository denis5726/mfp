package ru.mfp.auth.exception

import org.springframework.http.HttpStatus
import ru.mfp.common.exception.MfpApiException

class RegistrationException(message: String) : MfpApiException(HttpStatus.BAD_REQUEST, message)
