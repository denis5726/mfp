package ru.mfp.payment.exception

import org.springframework.http.HttpStatus
import ru.mfp.common.exception.MfpApiException

class PaymentCreatingException(message: String) : MfpApiException(HttpStatus.BAD_REQUEST, message)
