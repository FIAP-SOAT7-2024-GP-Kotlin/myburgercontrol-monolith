package io.github.soat7.myburguercontrol.service

import io.github.soat7.myburguercontrol.entities.User
import io.github.soat7.myburguercontrol.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository,
    private val enconder: PasswordEncoder
) {
    fun createUser(user: User): User? {
        val found = userRepository.findByEmail(user.email)

        if (found == null) {
            user.password = enconder.encode(user.password)
            return userRepository.save(user)
        } else {
            return null
        }
    }

    fun findByUUID(uuid: UUID): Optional<User> =
        userRepository.findById(uuid)

    fun findByAll(): List<User> =
        userRepository.findAll()

    fun deleteByUUID(uuid: UUID): Boolean {
        val fouded = findByUUID(uuid)
        if (fouded.isPresent) {
            userRepository.deleteById(uuid)
            return true
        } else {
            return false
        }
    }
}
