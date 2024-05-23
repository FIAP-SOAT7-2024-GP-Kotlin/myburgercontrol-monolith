package io.github.soat7.myburguercontrol.domain.service

import io.github.soat7.myburguercontrol.application.ports.inbound.UserServicePort
import io.github.soat7.myburguercontrol.application.ports.outbound.UserDatabasePort
import io.github.soat7.myburguercontrol.domain.model.User
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.UUID

class UserService(
    private val dataBasePort: UserDatabasePort,
    private val enconder: PasswordEncoder
) : UserServicePort {
    override fun create(user: User): User {
        user.password = enconder.encode(user.password)
        return dataBase.create(user)
    }

    override fun findUserById(id: UUID): User? {
        return dataBase.findUserById(id)
    }

    override fun findUserByCpf(cpf: String): User? {
        return dataBase.findUserByCpf(cpf)
    }

    override fun findByAll(): List<User> {
        return dataBase.findByAll()
    }

    override fun deleteByUUID(uuid: UUID): Boolean {
        val found = findUserById(uuid)
        if (found != null) {
            dataBase.deleteByUUID(uuid)
            return true
        } else {
            return false
        }
    }
}
