package io.github.soat7.myburguercontrol.infrastructure.rest.product.api

import java.math.BigDecimal
import java.util.UUID

data class ProductResponse(
    val id: UUID,
    val description: String,
    val price: BigDecimal,
    val type: String
)
