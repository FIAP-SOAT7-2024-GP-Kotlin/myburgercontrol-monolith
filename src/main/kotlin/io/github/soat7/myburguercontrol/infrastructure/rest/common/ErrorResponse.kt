package io.github.soat7.myburguercontrol.infrastructure.rest.common

data class ErrorResponse(
    val code: String,
    val messages: List<String>
)
