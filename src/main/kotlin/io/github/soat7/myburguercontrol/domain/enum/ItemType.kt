package io.github.soat7.myburguercontrol.domain.enum

import java.util.Locale

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
