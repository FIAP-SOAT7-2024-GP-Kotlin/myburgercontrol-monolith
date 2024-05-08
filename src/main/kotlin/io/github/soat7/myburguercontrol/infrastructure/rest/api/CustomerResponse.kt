package io.github.soat7.myburguercontrol.infrastructure.rest.api

import java.util.UUID

data class CustomerResponse(
    val id: UUID,
    val cpf: String,
    val name: String,
    val email: String
)
