package ru.mfp.user.exception

import org.springframework.http.HttpStatus
import ru.mfp.common.exception.MfpApiException

class AuthorizationException(message: String) : MfpApiException(HttpStatus.FORBIDDEN, message)
