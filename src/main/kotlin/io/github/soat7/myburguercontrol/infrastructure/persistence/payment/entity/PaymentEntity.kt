package io.github.soat7.myburguercontrol.infrastructure.persistence.payment.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(
    name = "payment",
    schema = "myburguer"
)
class PaymentEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: UUID,

    @Column(name = "status", length = 255, nullable = false)
    var status: String,

    @Column(name = "status", length = 255)
    var authorizationId: String? = null
)
