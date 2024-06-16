package io.github.soat7.myburguercontrol.business.service

import io.github.soat7.myburguercontrol.business.model.User
import io.github.soat7.myburguercontrol.business.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository,
    private val encoder: PasswordEncoder,
) {

    fun create(user: User): User {
        user.password = encoder.encode(user.password)
        return userRepository.create(user)
    }

    fun findUserById(id: UUID): User? {
        return userRepository.findUserById(id)
    }

    fun findUserByCpf(cpf: String): User? {
        return userRepository.findUserByCpf(cpf)
    }

    fun findByAll(): List<User> {
        return userRepository.findByAll()
    }

    fun deleteByUUID(uuid: UUID): Boolean {
        val found = findUserById(uuid)
        if (found != null) {
            userRepository.deleteByUUID(uuid)
            return true
        } else {
            return false
        }
    }
}
