package io.github.soat7.myburguercontrol.database.order.entity

import io.github.soat7.myburguercontrol.database.customer.entity.CustomerEntity
import io.github.soat7.myburguercontrol.database.payment.entity.PaymentEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(
    name = "order",
    schema = "myburguer",
)
class OrderEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", foreignKey = ForeignKey(name = "fk_customer"), nullable = false)
    var customer: CustomerEntity,

    @Column(name = "status", length = 255, nullable = false)
    var status: String,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant,

    @OneToMany(
        fetch = FetchType.EAGER,
        cascade = [CascadeType.ALL],
        mappedBy = "order",
        targetEntity = OrderItemEntity::class,
    )
    var items: List<OrderItemEntity> = mutableListOf(),

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", referencedColumnName = "id")
    var payment: PaymentEntity?,
)
