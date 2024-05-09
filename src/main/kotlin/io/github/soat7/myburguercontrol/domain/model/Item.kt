package io.github.soat7.myburguercontrol.domain.model

import java.math.BigDecimal
import java.util.UUID

data class Item(
    val id: UUID,
    val description: String,
    val price: BigDecimal
)
