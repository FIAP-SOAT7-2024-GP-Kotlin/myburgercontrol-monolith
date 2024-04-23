package io.github.soat7.myburguercontrol.entities

import com.google.common.base.MoreObjects
import com.google.common.base.Objects.equal
import com.google.common.base.Objects.hashCode
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(
    name = "client",
    schema = "myburguer",
    uniqueConstraints = [
        UniqueConstraint(name = "pk_client_id", columnNames = ["id"]),
        UniqueConstraint(name = "uk_client_cpf", columnNames = ["cpf"])
    ]
)
@SequenceGenerator(name = "sq_client_id", sequenceName = "sq_client_id", schema = "myburguer", allocationSize = 1)
class ClientEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_client_id")
    @Column(name = "id", nullable = false)
    var id: Long? = null,

    @Column(name = "cpf", length = 255, nullable = false)
    var cpf: String
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ClientEntity

        return this.id?.let { equal(this.id, other.id) } ?: equal(this.cpf, other.cpf)
    }

    override fun hashCode(): Int {
        return this.id?.let { hashCode(it) } ?: hashCode(cpf)
    }

    override fun toString(): String {
        return MoreObjects.toStringHelper(this)
            .add("id", id)
            .add("cpf", cpf)
            .toString()
    }
}
