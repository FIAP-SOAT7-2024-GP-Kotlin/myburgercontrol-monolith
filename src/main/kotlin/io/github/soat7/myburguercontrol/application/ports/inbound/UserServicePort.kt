package io.github.soat7.myburguercontrol.application.ports.inbound

import io.github.soat7.myburguercontrol.domain.model.User
import java.util.UUID

interface UserServicePort {

    fun create(user: User): User

    fun findUserById(id: UUID): User?

    fun findUserByCpf(cpf: String): User?
}
