package io.github.soat7.myburguercontrol.infrastructure.common

import com.fasterxml.jackson.core.JsonProcessingException
import io.github.soat7.myburguercontrol.domain.exception.ReasonCode
import io.github.soat7.myburguercontrol.domain.exception.ReasonCodeException
import io.github.soat7.myburguercontrol.infrastructure.rest.common.ErrorResponse
import mu.KLogging
import mu.withLoggingContext
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
@Order(-2)
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {

    companion object : KLogging()

    internal data class HttpError(
        val httpStatus: HttpStatus,
        val code: String,
        val messages: List<String>
    ) {
        constructor(reasonCode: ReasonCode) : this(reasonCode.status, reasonCode.code, listOf(reasonCode.description))
    }

    @ExceptionHandler(Throwable::class)
    fun handleException(t: Throwable): ResponseEntity<Any> {
        val error = when (t) {
            is ReasonCodeException -> HttpError(t.reasonCode)
            is ResponseStatusException -> HttpError(
                HttpStatus.valueOf(t.statusCode.value()),
                ReasonCode.UNEXPECTED_ERROR.code,
                listOf(t.reason ?: "Unexpected error")
            )

            else -> HttpError(ReasonCode.UNEXPECTED_ERROR)
        }
        log(t, error)

        val body: Any = try {
            ErrorResponse(error.code, error.messages)
        } catch (e: JsonProcessingException) {
            ""
        }

        return ResponseEntity
            .status(error.httpStatus)
            .contentType(MediaType.APPLICATION_JSON)
            .body(body)
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
