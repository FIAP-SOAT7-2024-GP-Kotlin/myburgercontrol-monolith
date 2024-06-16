package io.github.soat7.myburguercontrol.fixtures

import io.github.soat7.myburguercontrol.business.enum.UserRole
import io.github.soat7.myburguercontrol.business.model.User
import io.github.soat7.myburguercontrol.database.user.entity.UserEntity
import io.github.soat7.myburguercontrol.webservice.auth.api.UserCreationRequest
import java.util.UUID

object UserFixtures {

    fun mockUserEntity(
        id: UUID = UUID.randomUUID(),
        cpf: String,
        password: String = "pass123",
        userRole: UserRole = UserRole.USER,
    ) =
        UserEntity(
            id = id,
            cpf = cpf,
            password = password,
            role = userRole,
        )

    fun mockUserCreationRequest(cpf: String, password: String, userRole: UserRole) = UserCreationRequest(
        cpf = cpf,
        password = password,
        role = userRole,
    )

    fun mockUser(id: UUID = UUID.randomUUID(), cpf: String, password: String, userRole: UserRole): User {
        val entity = mockUserEntity(id = id, cpf = cpf, password = password, userRole = userRole)

        return User(
            id = entity.id,
            cpf = entity.cpf,
            password = entity.password,
            role = entity.role,
        )
    }
}
