package io.github.soat7.myburguercontrol.infrastructure.inbound.rest.api

import java.util.UUID

data class CustomerResponse(
    val id: UUID,
    val cpf: String,
    val name: String? = null,
    val email: String? = null
)
