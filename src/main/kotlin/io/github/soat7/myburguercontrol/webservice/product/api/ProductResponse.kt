package io.github.soat7.myburguercontrol.webservice.product.api

import java.math.BigDecimal
import java.util.UUID

data class ProductResponse(
    val id: UUID,
    val name: String,
    val description: String,
    val price: BigDecimal,
    val type: String,
)
