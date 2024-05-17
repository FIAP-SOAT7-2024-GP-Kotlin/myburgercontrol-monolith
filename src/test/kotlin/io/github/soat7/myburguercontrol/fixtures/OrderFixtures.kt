package io.github.soat7.myburguercontrol.fixtures

import io.github.soat7.myburguercontrol.domain.model.Customer
import io.github.soat7.myburguercontrol.domain.model.Order
import io.github.soat7.myburguercontrol.domain.model.OrderItem
import io.github.soat7.myburguercontrol.fixtures.ProductFixtures.mockDomainProduct
import java.util.UUID

object OrderFixtures {

    fun mockOrder(
        customer: Customer,
        numItems: Int = 1,
    ) = Order(
        id = UUID.randomUUID(),
        customer = customer
    ).apply {
        repeat(numItems) {
            val productId = (1L..1000L).random()
            this.items.add(
                OrderItem(
                    id = productId,
                    product = mockDomainProduct(description = "Product $productId"),
                    quantity = 1,
                )
            )
        }
    }
}
