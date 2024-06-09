package io.github.soat7.myburguercontrol.domain.model

import io.github.soat7.myburguercontrol.domain.enum.UserRole
import java.util.UUID

data class User(
    val id: UUID,
    val cpf: String,
    var password: String,
    val role: UserRole
)
