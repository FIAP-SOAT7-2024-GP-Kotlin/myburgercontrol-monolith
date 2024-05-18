package io.github.soat7.myburguercontrol.infrastructure.common

import io.github.soat7.myburguercontrol.domain.exception.ReasonCode
import io.github.soat7.myburguercontrol.domain.exception.ReasonCodeException
import mu.KLogging
import mu.withLoggingContext
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.net.URI

@ControllerAdvice
@Order(-2)
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {

    companion object : KLogging()

    internal data class HttpError(
        val httpStatus: HttpStatus,
        val code: String,
        val message: String
    ) {
        constructor(reasonCode: ReasonCode) : this(reasonCode.status, reasonCode.code, reasonCode.description)
    }

    @ExceptionHandler(Throwable::class)
    fun handleException(t: Throwable, request: WebRequest): ResponseEntity<ProblemDetail> {
        val error = when (t) {
            is ReasonCodeException -> HttpError(t.reasonCode)
            is ResponseStatusException -> HttpError(
                HttpStatus.valueOf(t.statusCode.value()),
                ReasonCode.UNEXPECTED_ERROR.code,
                t.reason ?: "An unexpected error occurred"
            )

            else -> HttpError(ReasonCode.UNEXPECTED_ERROR)
        }
        log(t, error)

        val problemDetail = ProblemDetail.forStatusAndDetail(
            error.httpStatus,
            error.message
        ).apply {
            title = error.httpStatus.reasonPhrase
            detail = t.message ?: "Unexpected  error occurred"
            instance = URI.create(request.getDescription(false))
        }

        return ResponseEntity
            .status(error.httpStatus)
            .contentType(MediaType.APPLICATION_JSON)
            .body(problemDetail)
    }

    private fun log(t: Throwable, error: HttpError) {
        withLoggingContext(
            "exception" to t.javaClass.simpleName,
            "status" to error.httpStatus.value().toString(),
            "code" to error.code,
            "message" to t.message,
            "cause" to t.cause?.message
        ) {
            if (error.httpStatus == HttpStatus.INTERNAL_SERVER_ERROR) {
                logger.error { "Unexpected error occurred while processing request" }
            } else {
                logger.warn { "An error occurred while processing request" }
            }
        }
    }
}
