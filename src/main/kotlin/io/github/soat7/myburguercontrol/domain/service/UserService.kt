package io.github.soat7.myburguercontrol.domain.service

import io.github.soat7.myburguercontrol.application.ports.inbound.UserServicePort
import io.github.soat7.myburguercontrol.application.ports.outbound.UserDatabasePort
import io.github.soat7.myburguercontrol.domain.model.User
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.UUID

class UserService(
    private val userDatabasePort: UserDatabasePort,
    private val enconder: PasswordEncoder
) : UserServicePort {
    override fun create(user: User): User {
        user.password = enconder.encode(user.password)
        return userDatabasePort.create(user)
    }

    override fun findUserById(id: UUID): User? {
        return userDatabasePort.findUserById(id)
    }

    override fun findUserByCpf(cpf: String): User? {
        return userDatabasePort.findUserByCpf(cpf)
    }

    override fun findByAll(): List<User> {
        return userDatabasePort.findByAll()
    }

    override fun deleteByUUID(uuid: UUID): Boolean {
        val found = findUserById(uuid)
        if (found != null) {
            userDatabasePort.deleteByUUID(uuid)
            return true
        } else {
            return false
        }
    }
}
