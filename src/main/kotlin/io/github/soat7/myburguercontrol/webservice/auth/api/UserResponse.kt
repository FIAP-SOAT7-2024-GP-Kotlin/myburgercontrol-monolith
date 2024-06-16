package io.github.soat7.myburguercontrol.webservice.auth.api

import io.github.soat7.myburguercontrol.business.enum.UserRole
import java.util.UUID

data class UserResponse(
    val id: UUID,
    val cpf: String,
    val role: UserRole,
)
