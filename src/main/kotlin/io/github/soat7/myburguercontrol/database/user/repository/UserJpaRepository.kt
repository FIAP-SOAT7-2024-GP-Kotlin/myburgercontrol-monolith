package io.github.soat7.myburguercontrol.database.user.repository

import io.github.soat7.myburguercontrol.database.user.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserRepository : JpaRepository<UserEntity, UUID> {

    fun findByCpf(email: String): UserEntity?
}
