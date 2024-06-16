package io.github.soat7.myburguercontrol.business.mapper

import io.github.soat7.myburguercontrol.business.enum.OrderStatus
import io.github.soat7.myburguercontrol.business.model.Order
import io.github.soat7.myburguercontrol.business.model.OrderDetail
import io.github.soat7.myburguercontrol.business.model.OrderItem
import io.github.soat7.myburguercontrol.database.customer.entity.CustomerEntity
import io.github.soat7.myburguercontrol.database.order.entity.OrderEntity
import io.github.soat7.myburguercontrol.database.order.entity.OrderItemEntity
import io.github.soat7.myburguercontrol.database.payment.entity.PaymentEntity
import io.github.soat7.myburguercontrol.database.product.entity.ProductEntity
import io.github.soat7.myburguercontrol.thirdparty.api.PaymentIntegrationRequest
import io.github.soat7.myburguercontrol.webservice.order.api.request.OrderCreationRequest
import io.github.soat7.myburguercontrol.webservice.order.api.response.OrderItemResponse
import io.github.soat7.myburguercontrol.webservice.order.api.response.OrderResponse
import java.util.UUID

fun OrderCreationRequest.toOrderDetails() = OrderDetail(
    customerCpf = this.customerCpf,
    items = this.items.map {
        OrderDetail.OrderItemDetail(
            productId = it.productId,
            quantity = it.quantity,
            comment = it.comment,
        )
    },
)

fun Order.toResponse() = OrderResponse(
    id = this.id,
    customer = this.customer.toResponse(),
    status = this.status,
    createdAt = this.createdAt,
    total = this.total,
).apply {
    this.items.addAll(
        this@toResponse.items.map {
            OrderItemResponse(
                product = it.product.toOrderItemProductResponse(),
                quantity = it.quantity,
                comment = it.comment,
            )
        },
    )
}

fun Order.toPersistence(
    customerEntity: CustomerEntity,
    paymentEntity: PaymentEntity?,
    productFinder: (productId: UUID) -> ProductEntity,
) = OrderEntity(
    id = this.id,
    customer = customerEntity,
    status = this.status.name,
    createdAt = this.createdAt,
    payment = paymentEntity,
).apply {
    this.items = this@toPersistence.items.map { it.toPersistence(this, productFinder) }
}

fun OrderItem.toPersistence(orderEntity: OrderEntity, productFinder: (productId: UUID) -> ProductEntity) =
    OrderItemEntity(
        id = this.id,
        order = orderEntity,
        product = productFinder.invoke(this.product.id),
        quantity = this.quantity,
        comment = this.comment,
    )

fun OrderEntity.toDomain() = Order(
    id = this.id ?: UUID.randomUUID(),
    customer = this.customer.toDomain(),
    status = OrderStatus.from(this.status),
    createdAt = this.createdAt,
    items = this.items.map { it.toDomain() },
    payment = this.payment?.toDomain(),
)

fun OrderItemEntity.toDomain() = OrderItem(
    id = this.id ?: UUID.randomUUID(),
    product = this.product.toDomain(),
    quantity = this.quantity,
    comment = this.comment,
)

fun Order.toPaymentRequest() = PaymentIntegrationRequest(
    id = this.id.toString(),
    cpf = this.customer.cpf,
    value = this.total.toString(),
)
