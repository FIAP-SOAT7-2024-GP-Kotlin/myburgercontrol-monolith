package io.github.soat7.myburguercontrol.domain.exception

class ReasonCodeException(
    val reasonCode: ReasonCode,
    cause: Throwable? = null
) : RuntimeException(reasonCode.description, cause)
