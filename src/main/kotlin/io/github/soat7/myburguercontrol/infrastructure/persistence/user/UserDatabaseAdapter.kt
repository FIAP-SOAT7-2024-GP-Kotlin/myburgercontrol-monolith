package io.github.soat7.myburguercontrol.infrastructure.persistence.user

import io.github.soat7.myburguercontrol.application.ports.outbound.UserDatabasePort
import io.github.soat7.myburguercontrol.domain.mapper.toDomain
import io.github.soat7.myburguercontrol.domain.mapper.toPersistence
import io.github.soat7.myburguercontrol.domain.model.User
import io.github.soat7.myburguercontrol.infrastructure.persistence.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class UserDatabaseAdapter(
    private val repository: UserRepository
) : UserDatabasePort {

    override fun create(user: User): User = repository.save(user.toPersistence()).toDomain()

    override fun findUserByCpf(cpf: String): User? = repository.findByCpf(cpf)?.toDomain()

    override fun findUserById(id: UUID): User? = repository.findByIdOrNull(id)?.toDomain()

    override fun findByAll(): List<User> = repository.findAll().map { it.toDomain() }

    override fun deleteByUUID(uuid: UUID): Boolean {
        repository.deleteById(uuid)
        return true
    }
}
