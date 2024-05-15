package io.github.soat7.myburguercontrol.infrastructure.rest.item.api

import java.math.BigDecimal
import java.util.UUID

data class ItemResponse(
    val id: UUID,
    val description: String,
    val price: BigDecimal,
    val type: String
)
