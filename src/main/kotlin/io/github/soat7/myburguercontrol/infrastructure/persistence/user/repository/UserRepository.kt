package io.github.soat7.myburguercontrol.infrastructure.persistence.user.repository

import io.github.soat7.myburguercontrol.infrastructure.persistence.user.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserRepository : JpaRepository<UserEntity, UUID> {

    fun findByCpf(email: String): UserEntity?
}
