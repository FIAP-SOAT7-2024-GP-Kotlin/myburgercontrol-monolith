package io.github.soat7.myburguercontrol.infrastructure.persistence.customer.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.util.UUID

@Entity
@Table(
    name = "customer",
    schema = "myburguer",
    uniqueConstraints = [
        UniqueConstraint(name = "uk_customer_cpf", columnNames = ["cpf"])
    ]
)
data class CustomerEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: UUID? = null,

    @Column(name = "cpf", length = 255, nullable = false)
    var cpf: String,

    @Column(name = "name", length = 255)
    var name: String,

    @Column(name = "email", length = 255)
    var email: String
)
