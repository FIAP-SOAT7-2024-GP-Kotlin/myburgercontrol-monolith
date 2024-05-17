package io.github.soat7.myburguercontrol.infrastructure.rest.customer.api.response

import io.github.soat7.myburguercontrol.infrastructure.rest.product.api.ProductResponse

class OrderItemResponse(
    val id: Long,
    val productResponse: ProductResponse,
    val quantity: Int
)
