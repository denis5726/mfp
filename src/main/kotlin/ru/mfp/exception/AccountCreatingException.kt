package ru.mfp.exception

import org.springframework.http.HttpStatus

class AccountCreatingException(status: HttpStatus, message: String) : MfpServerException(status, message)