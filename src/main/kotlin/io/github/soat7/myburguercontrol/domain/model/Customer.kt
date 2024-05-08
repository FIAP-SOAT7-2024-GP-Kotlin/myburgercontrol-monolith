package io.github.soat7.myburguercontrol.domain.model

import java.util.UUID

data class Customer(
    val id: UUID,
    val cpf: String,
    val name: String,
    val email: String
)
