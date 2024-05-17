package io.github.soat7.myburguercontrol.domain.mapper

import io.github.soat7.myburguercontrol.domain.enum.OrderStatus
import io.github.soat7.myburguercontrol.domain.model.Order
import io.github.soat7.myburguercontrol.domain.model.OrderItem
import io.github.soat7.myburguercontrol.infrastructure.persistence.customer.entity.CustomerEntity
import io.github.soat7.myburguercontrol.infrastructure.persistence.order.entity.OrderEntity
import io.github.soat7.myburguercontrol.infrastructure.persistence.order.entity.OrderItemEntity
import io.github.soat7.myburguercontrol.infrastructure.persistence.product.entity.ProductEntity
import io.github.soat7.myburguercontrol.infrastructure.rest.customer.api.response.OrderItemResponse
import io.github.soat7.myburguercontrol.infrastructure.rest.customer.api.response.OrderResponse
import java.util.UUID

fun Order.toResponse() = OrderResponse(
    id = this.id,
    customer = this.customer.toResponse(),
    status = this.status,
    createdAt = this.createdAt,
    total = this.total
).apply {
    this.items.addAll(this@toResponse.items.map {
        OrderItemResponse(
            id = it.id,
            productResponse = it.product.toResponse(),
            quantity = it.quantity
        )
    })
}

fun Order.toPersistence(customerEntity: CustomerEntity, productFinder: (productId: UUID) -> ProductEntity) =
    OrderEntity(
        id = this.id,
        customer = customerEntity,
        status = this.status.name,
        createdAt = this.createdAt,
    ).apply {
        this.items = this@toPersistence.items.map { it.toPersistence(this, productFinder) }
    }

fun OrderItem.toPersistence(orderEntity: OrderEntity, productFinder: (productId: UUID) -> ProductEntity) =
    OrderItemEntity(
        id = this.id,
        order = orderEntity,
        product = productFinder.invoke(this.product.id),
        quantity = this.quantity
    )

fun OrderEntity.toDomain() = Order(
    id = this.id ?: UUID.fromString(""),
    customer = this.customer.toDomain(),
    status = OrderStatus.from(this.status),
    createdAt = this.createdAt
).apply {
    this.items.addAll(
        this@toDomain.items.map { it.toDomain() }
    )
}

fun OrderItemEntity.toDomain() = OrderItem(
    id = this.id ?: 0,
    product = this.product.toDomain(),
    quantity = this.quantity
)
