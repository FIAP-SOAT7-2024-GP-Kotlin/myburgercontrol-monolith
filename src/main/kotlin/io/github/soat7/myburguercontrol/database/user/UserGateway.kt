package io.github.soat7.myburguercontrol.database.user

import io.github.soat7.myburguercontrol.business.mapper.toDomain
import io.github.soat7.myburguercontrol.business.mapper.toPersistence
import io.github.soat7.myburguercontrol.business.model.User
import io.github.soat7.myburguercontrol.business.repository.UserRepository
import io.github.soat7.myburguercontrol.database.user.repository.UserJpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class UserGateway(
    private val repository: UserJpaRepository,
) : UserRepository {

    override fun create(user: User): User = repository.save(user.toPersistence()).toDomain()

    override fun findUserByCpf(cpf: String): User? = repository.findByCpf(cpf)?.toDomain()

    override fun findUserById(id: UUID): User? = repository.findByIdOrNull(id)?.toDomain()

    override fun findByAll(): List<User> = repository.findAll().map { it.toDomain() }

    override fun deleteByUUID(uuid: UUID): Boolean {
        repository.deleteById(uuid)
        return true
    }
}
