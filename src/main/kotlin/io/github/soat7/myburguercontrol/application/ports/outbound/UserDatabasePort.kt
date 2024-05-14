package io.github.soat7.myburguercontrol.application.ports.outbound

import io.github.soat7.myburguercontrol.domain.model.User
import java.util.UUID

interface UserDatabasePort {

    fun create(user: User): User

    fun findUserById(id: UUID): User?

    fun findUserByCpf(cpf: String): User?
}
