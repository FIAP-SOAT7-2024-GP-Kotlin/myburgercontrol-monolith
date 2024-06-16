package io.github.soat7.myburguercontrol.business.mapper

import io.github.soat7.myburguercontrol.business.model.User
import io.github.soat7.myburguercontrol.database.user.entity.UserEntity
import io.github.soat7.myburguercontrol.webservice.auth.api.UserCreationRequest
import io.github.soat7.myburguercontrol.webservice.auth.api.UserResponse
import java.util.UUID

fun UserCreationRequest.toDomain() = User(
    id = UUID.randomUUID(),
    cpf = this.cpf,
    password = this.password,
    role = this.role,
)

fun UserEntity.toDomain() = User(
    id = this.id,
    cpf = this.cpf,
    password = this.password,
    role = this.role,
)

fun User.toPersistence() = UserEntity(
    id = this.id,
    cpf = this.cpf,
    password = this.password,
    role = this.role,
)

fun User.toResponse() = UserResponse(
    id = this.id,
    cpf = this.cpf,
    role = this.role,
)
