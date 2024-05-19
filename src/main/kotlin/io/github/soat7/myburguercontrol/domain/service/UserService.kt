package io.github.soat7.myburguercontrol.domain.service

import io.github.soat7.myburguercontrol.application.ports.inbound.UserServicePort
import io.github.soat7.myburguercontrol.application.ports.outbound.UserDatabasePort
import io.github.soat7.myburguercontrol.domain.model.User
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.UUID

class UserService (
    private val dataBasePort: UserDatabasePort,
    private val enconder: PasswordEncoder
) : UserServicePort {
    override fun create(user: User): User {
        user.password = enconder.encode(user.password)
        return dataBasePort.create(user)
    }

    override fun findUserById(id: UUID): User? {
        return dataBasePort.findUserById(id)
    }

    override fun findUserByCpf(cpf: String): User? {
        return dataBasePort.findUserByCpf(cpf)
    }

    override fun findByAll(): List<User> {
        return dataBasePort.findByAll()
    }

    override fun deleteByUUID(uuid: UUID): Boolean {
        val founded = findUserById(uuid)
        if (founded != null) {
            dataBasePort.deleteByUUID(uuid)
            return true
        }else {
            return false
        }
    }

}
