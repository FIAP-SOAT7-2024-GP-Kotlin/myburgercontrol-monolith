package io.github.soat7.myburguercontrol.domain.service

import io.github.soat7.myburguercontrol.application.ports.inbound.UserServicePort
import io.github.soat7.myburguercontrol.application.ports.outbound.UserDatabasePort
import io.github.soat7.myburguercontrol.domain.model.User
import java.util.UUID

class UserService (
    private val dataBasePort: UserDatabasePort
) : UserServicePort {
    override fun create(user: User): User {
        return dataBasePort.create(user)
    }

    override fun findUserById(id: UUID): User? {
        return dataBasePort.findUserById(id)
    }

    override fun findUserByCpf(cpf: String): User? {
        return dataBasePort.findUserByCpf(cpf)
    }

}
