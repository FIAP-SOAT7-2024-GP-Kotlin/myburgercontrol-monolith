package io.github.soat7.myburguercontrol.fixtures

import io.github.soat7.myburguercontrol.domain.model.Role
import io.github.soat7.myburguercontrol.domain.model.User
import io.github.soat7.myburguercontrol.infrastructure.persistence.user.entity.UserEntity
import io.github.soat7.myburguercontrol.infrastructure.rest.api.UserCreationRequest
import java.util.UUID

object UserFixtures {

    fun mockUserEntity(
        id: UUID = UUID.randomUUID(),
        cpf: String,
        password: String = "pass123",
        role: Role = Role.USER,
    ) =
        UserEntity(
            id = id,
            cpf = cpf,
            password = password,
            role = role
        )

    fun mockUserCreationRequest(cpf: String, password: String, role: Role) = UserCreationRequest(
        cpf = cpf,
        password = password,
        role = role
    )

    fun mockUser(id: UUID = UUID.randomUUID(), cpf: String, password: String, role: Role): User {
        val entity = mockUserEntity(id = id, cpf = cpf, password = password, role = role)

        return User(
            id = entity.id,
            cpf = entity.cpf,
            password = entity.password,
            role = entity.role
        )

    }
}
