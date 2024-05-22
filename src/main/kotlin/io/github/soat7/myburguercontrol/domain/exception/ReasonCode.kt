package io.github.soat7.myburguercontrol.domain.exception

import org.springframework.http.HttpStatus

enum class ReasonCode(
    val status: HttpStatus,
    val code: String,
    val description: String
) {
    UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "01", "An unexpected error occurred"),

    CPF_ALREADY_REGISTERED(HttpStatus.BAD_REQUEST, "10", "The given document is already registered"),

    PAYMENT_INTEGRATION_ERROR(HttpStatus.UNPROCESSABLE_ENTITY, "20", "The payment provider did not return the required values")
}
