package io.github.soat7.myburguercontrol.business.repository

import io.github.soat7.myburguercontrol.business.model.User
import java.util.UUID

interface UserRepository {

    fun create(user: User): User

    fun findUserById(id: UUID): User?

    fun findUserByCpf(cpf: String): User?

    fun findByAll(): List<User>

    fun deleteByUUID(uuid: UUID): Boolean
}
