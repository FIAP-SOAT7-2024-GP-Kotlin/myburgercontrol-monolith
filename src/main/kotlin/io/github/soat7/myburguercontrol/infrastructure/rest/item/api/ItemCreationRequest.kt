package io.github.soat7.myburguercontrol.infrastructure.rest.item.api

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class ItemCreationRequest(
    @NotBlank
    val description: String,

    @NotNull
    val price: BigDecimal,

    val type: ItemType
) {
    enum class ItemType {
        APPETIZER,
        DESSERT,
        DRINK,
        FOOD,
        OTHER
    }
}
