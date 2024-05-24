package io.github.soat7.myburguercontrol.infrastructure.rest.user.api

import io.github.soat7.myburguercontrol.domain.model.Role
import java.util.UUID

data class UserResponse(
    val id: UUID,
    val cpf: String,
    val role: Role
)
