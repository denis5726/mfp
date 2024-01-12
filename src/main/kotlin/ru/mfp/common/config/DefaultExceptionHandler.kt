package ru.mfp.common.config

import jakarta.servlet.http.HttpServletRequest
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import ru.mfp.common.dto.ErrorResponseDto
import ru.mfp.common.exception.MfpApiException
import java.time.LocalDateTime
import java.util.*

private val log = KotlinLogging.logger { }

@ControllerAdvice
class DefaultExceptionHandler {

    @ExceptionHandler(MfpApiException::class)
    fun handleApplicationException(
        exception: MfpApiException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponseDto> = handleException(exception.status, request, exception)

    @ExceptionHandler(Throwable::class)
    fun handleOtherException(
        exception: Throwable,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponseDto> = handleException(HttpStatus.INTERNAL_SERVER_ERROR, request, exception)

    fun handleException(
        status: HttpStatus,
        request: HttpServletRequest,
        exception: Throwable
    ): ResponseEntity<ErrorResponseDto> {
        log.info { "Request exception: ${exception::class}, message: ${exception.message}" }
        exception.printStackTrace(System.out)
        return ResponseEntity
            .status(status)
            .body(
                ErrorResponseDto(
                    status,
                    exception.message ?: "Undefined error",
                    LocalDateTime.now(),
                    resolveRequestPath(request)
                )
            )
    }

    private fun resolveRequestPath(request: HttpServletRequest): String {
        return if (request.queryString != null) String.format(
            "%s?%s",
            request.requestURI,
            request.queryString
        ) else request.requestURI
    }
}
