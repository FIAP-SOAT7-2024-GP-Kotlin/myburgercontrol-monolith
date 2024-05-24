package io.github.soat7.myburguercontrol.infrastructure.rest.user.api

import io.github.soat7.myburguercontrol.domain.model.Role
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UserCreationRequest(
    @NotBlank
    @Size(max = 11)
    val cpf: String,

    @NotBlank
    val password: String,

    @NotBlank
    val role: Role
)
