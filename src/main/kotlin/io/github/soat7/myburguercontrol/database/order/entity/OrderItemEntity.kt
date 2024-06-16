package io.github.soat7.myburguercontrol.database.order.entity

import io.github.soat7.myburguercontrol.database.product.entity.ProductEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(
    name = "order_item",
    schema = "myburguer",
)
class OrderItemEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", foreignKey = ForeignKey(name = "fk_order"))
    var order: OrderEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = ForeignKey(name = "fk_product"), nullable = false)
    var product: ProductEntity,

    @Column(name = "quantity", length = 4, nullable = false)
    var quantity: Int,

    @Column(name = "comment", length = 1000)
    var comment: String? = null,
)
