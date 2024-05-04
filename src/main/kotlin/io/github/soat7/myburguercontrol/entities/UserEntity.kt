package io.github.soat7.myburguercontrol.entities

import com.google.common.base.MoreObjects
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
        UniqueConstraint(name = "pk_user_uuid", columnNames = ["uuid"])
    ]
)
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", nullable = false, columnDefinition = "uuid")
    val uuid: UUID,

    @Column(name = "email", length = 255, nullable = false)
    var email: String,

    @Column(name = "password", length = 255, nullable = false)
    var password: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 20, nullable = false)
    var role: Role
) {

    override fun toString(): String {
        return MoreObjects.toStringHelper(this)
            .add("uuid", uuid)
            .add("email", email)
            .toString()
    }
}

enum class Role {
    USER,
    ADMIN
}
