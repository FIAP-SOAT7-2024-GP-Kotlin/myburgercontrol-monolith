package io.github.soat7.myburguercontrol.fixtures

import io.github.soat7.myburguercontrol.domain.enum.OrderStatus
import io.github.soat7.myburguercontrol.domain.model.Customer
import io.github.soat7.myburguercontrol.domain.model.Order
import io.github.soat7.myburguercontrol.domain.model.OrderItem
import io.github.soat7.myburguercontrol.fixtures.ProductFixtures.mockDomainProduct
import io.github.soat7.myburguercontrol.infrastructure.persistence.customer.entity.CustomerEntity
import io.github.soat7.myburguercontrol.infrastructure.persistence.order.entity.OrderEntity
import io.github.soat7.myburguercontrol.infrastructure.persistence.order.entity.OrderItemEntity
import io.github.soat7.myburguercontrol.infrastructure.persistence.payment.entity.PaymentEntity
import io.github.soat7.myburguercontrol.infrastructure.persistence.product.entity.ProductEntity
import java.time.Instant
import java.util.UUID

object OrderFixtures {

    fun mockOrder(
        customer: Customer
    ) = Order(
        id = UUID.randomUUID(),
        customer = customer
    ).apply {
        val productId = UUID.randomUUID()
        this.items.map {
            OrderItem(
                id = productId,
                product = mockDomainProduct(description = "Product $productId"),
                quantity = 1
            )
        }
    }

    fun mockOrderEntity(
        customerEntity: CustomerEntity,
        product: ProductEntity,
        paymentEntity: PaymentEntity
    ) = OrderEntity(
        id = UUID.randomUUID(),
        customer = customerEntity,
        status = OrderStatus.NEW.name,
        createdAt = Instant.now(),
        payment = paymentEntity
    ).apply {
        this.items = listOf(
            OrderItemEntity(
                id = UUID.randomUUID(),
                this,
                product = product,
                quantity = 1
            )
        )
    }
}
