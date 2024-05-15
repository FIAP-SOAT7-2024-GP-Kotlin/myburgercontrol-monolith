package io.github.soat7.myburguercontrol.infrastructure.rest.item.api

import io.github.soat7.myburguercontrol.domain.enum.ItemType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class ItemCreationRequest(
    @NotBlank
    val description: String,

    @NotNull
    val price: BigDecimal,

    @NotBlank
    val type: ItemType
)
