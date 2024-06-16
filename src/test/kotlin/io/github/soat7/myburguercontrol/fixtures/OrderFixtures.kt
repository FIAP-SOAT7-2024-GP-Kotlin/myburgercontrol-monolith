package io.github.soat7.myburguercontrol.fixtures

import io.github.soat7.myburguercontrol.business.enum.OrderStatus
import io.github.soat7.myburguercontrol.business.model.Customer
import io.github.soat7.myburguercontrol.business.model.Order
import io.github.soat7.myburguercontrol.business.model.OrderItem
import io.github.soat7.myburguercontrol.business.model.Payment
import io.github.soat7.myburguercontrol.database.customer.entity.CustomerEntity
import io.github.soat7.myburguercontrol.database.order.entity.OrderEntity
import io.github.soat7.myburguercontrol.database.order.entity.OrderItemEntity
import io.github.soat7.myburguercontrol.database.payment.entity.PaymentEntity
import io.github.soat7.myburguercontrol.database.product.entity.ProductEntity
import io.github.soat7.myburguercontrol.fixtures.ProductFixtures.mockDomainProduct
import java.time.Instant
import java.util.UUID

object OrderFixtures {

    fun mockOrder(
        customer: Customer,
    ) = Order(
        id = UUID.randomUUID(),
        customer = customer,
        payment = Payment(),
    ).apply {
        val productId = UUID.randomUUID()
        this.items.map {
            OrderItem(
                id = productId,
                product = mockDomainProduct(description = "Product $productId"),
                quantity = 1,
            )
        }
    }

    fun mockOrderEntity(
        customerEntity: CustomerEntity,
        product: ProductEntity,
        paymentEntity: PaymentEntity,
    ) = OrderEntity(
        id = UUID.randomUUID(),
        customer = customerEntity,
        status = OrderStatus.NEW.name,
        createdAt = Instant.now(),
        payment = paymentEntity,
    ).apply {
        this.items = listOf(
            OrderItemEntity(
                id = UUID.randomUUID(),
                this,
                product = product,
                quantity = 1,
            ),
        )
    }
}
