package io.github.soat7.myburguercontrol.database.user.entity

import io.github.soat7.myburguercontrol.business.enum.UserRole
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.util.UUID

@Entity
@Table(
    name = "user",
    schema = "myburguer",
    uniqueConstraints = [
        UniqueConstraint(name = "pk_user_id", columnNames = ["id"]),
    ],
)
data class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    val id: UUID,

    @Column(name = "cpf", length = 255, nullable = false)
    var cpf: String,

    @Column(name = "password", length = 255, nullable = false)
    var password: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 20, nullable = false)
    var role: UserRole,
)
