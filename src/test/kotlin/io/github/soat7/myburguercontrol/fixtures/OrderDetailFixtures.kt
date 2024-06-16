package io.github.soat7.myburguercontrol.fixtures

import io.github.soat7.myburguercontrol.business.model.OrderDetail
import io.github.soat7.myburguercontrol.business.model.Product

object OrderDetailFixtures {

    fun mockOrderDetail(
        cpf: String,
        product: Product = ProductFixtures.mockDomainProduct(),
    ) = OrderDetail(
        customerCpf = cpf,
        items = listOf(
            OrderDetail.OrderItemDetail(
                productId = product.id,
                quantity = 1,
            ),
        ),
    )
}
