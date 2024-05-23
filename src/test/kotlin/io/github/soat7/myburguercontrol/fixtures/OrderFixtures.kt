package io.github.soat7.myburguercontrol.fixtures

import io.github.soat7.myburguercontrol.domain.model.Customer
import io.github.soat7.myburguercontrol.domain.model.Order
import io.github.soat7.myburguercontrol.domain.model.OrderItem
import io.github.soat7.myburguercontrol.fixtures.ProductFixtures.mockDomainProduct
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
}
