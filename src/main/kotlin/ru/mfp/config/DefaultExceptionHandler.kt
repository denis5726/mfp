package ru.mfp.config

import ru.mfp.dto.ErrorResponseDto
import ru.mfp.exception.MfpServerException
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.LocalDateTime
import java.util.*

@ControllerAdvice
class DefaultExceptionHandler(
    private val log: Logger = LoggerFactory.getLogger(DefaultExceptionHandler::class.java)
) {

    @ExceptionHandler(MfpServerException::class)
    fun handleCommonException(exception: MfpServerException, request: HttpServletRequest): ResponseEntity<ErrorResponseDto> {
        log(exception)
        return ResponseEntity
            .status(exception.status)
            .body(ErrorResponseDto(
                exception.status, exception.message ?: "Undefined error", LocalDateTime.now(), resolveRequestPath(request)))
    }

    private fun log(throwable: Throwable) {
        log.info("Request exception: {}, message: {}", throwable.javaClass.simpleName, throwable.message)
        throwable.printStackTrace()
    }

    private fun resolveRequestPath(request: HttpServletRequest): String {
        return if (request.queryString != null) String.format(
            "%s?%s",
            request.requestURI,
            request.queryString
        ) else request.requestURI
    }
}
