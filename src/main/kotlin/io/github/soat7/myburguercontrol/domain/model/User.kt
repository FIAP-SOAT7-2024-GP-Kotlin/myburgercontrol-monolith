package io.github.soat7.myburguercontrol.domain.model

import java.util.UUID

data class User(
val id: UUID,
val cpf: String,
val password: String,
val role : Role
)
