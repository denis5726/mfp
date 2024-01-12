package ru.mfp.auth.exception

import org.springframework.http.HttpStatus
import ru.mfp.common.exception.MfpApiException

class AuthorizationException(message: String) : MfpApiException(HttpStatus.FORBIDDEN, message)
