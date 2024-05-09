package io.github.soat7.myburguercontrol.infrastructure.persistence.item.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal
import java.util.UUID

@Entity
@Table(
    name = "item",
    schema = "myburguer"
)
data class ItemEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    val id: UUID? = null,

    @Column(name = "description", nullable = false, length = 255)
    val description: String,

    @Column(name = "type", nullable = false, length = 255)
    @Enumerated(EnumType.STRING)
    val type: ItemType,

    @Column(name = "price", nullable = false, length = 255, scale = 2)
    val price: BigDecimal,
) {
    enum class ItemType {
        APPETIZER,
        DESSERT,
        DRINK,
        FOOD,
        OTHER
    }
}
