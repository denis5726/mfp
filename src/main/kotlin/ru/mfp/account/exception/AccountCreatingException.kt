package ru.mfp.account.exception

import org.springframework.http.HttpStatus
import ru.mfp.common.exception.MfpApiException

class AccountCreatingException(message: String) : MfpApiException(HttpStatus.BAD_REQUEST, message)
