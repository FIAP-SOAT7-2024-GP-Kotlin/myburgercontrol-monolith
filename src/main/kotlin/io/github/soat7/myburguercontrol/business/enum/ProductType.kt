package io.github.soat7.myburguercontrol.business.enum

import java.util.Locale

enum class ProductType {
    APPETIZER,
    DESSERT,
    DRINK,
    FOOD,
    OTHER,
    ;

    companion object {
        fun from(resource: String): ProductType {
            return try {
                ProductType.valueOf(resource.uppercase(Locale.getDefault()))
            } catch (ex: IllegalArgumentException) {
                throw ex
            }
        }
    }
}
