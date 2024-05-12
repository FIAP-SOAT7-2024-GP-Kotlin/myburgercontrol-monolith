package io.github.soat7.myburguercontrol.domain.model

import java.math.BigDecimal
import java.util.Locale
import java.util.UUID

data class Item(
    val id: UUID,
    val description: String,
    val price: BigDecimal,
    val type: ItemType
) {
    enum class ItemType {
        APPETIZER,
        DESSERT,
        DRINK,
        FOOD,
        OTHER;

        companion object {
            fun from(resource: String): ItemType {
                return try {
                    ItemType.valueOf(resource.uppercase(Locale.getDefault()))
                } catch (ex: IllegalArgumentException) {
                    throw ex
                }
            }
        }
    }
}
