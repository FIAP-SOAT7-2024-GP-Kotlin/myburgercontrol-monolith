package io.github.soat7.myburguercontrol.infrastructure.rest.auth.api

import io.github.soat7.myburguercontrol.domain.enum.UserRole
import java.util.UUID

data class UserResponse(
    val id: UUID,
    val cpf: String,
    val role: UserRole
)
