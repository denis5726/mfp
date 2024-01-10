package ru.mfp.exception

import org.springframework.http.HttpStatus

class DepositCreatingException(status: HttpStatus, message: String) : MfpServerException(status, message)