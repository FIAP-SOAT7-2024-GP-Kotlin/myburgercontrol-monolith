package io.github.soat7.myburguercontrol.business.repository

import io.github.soat7.myburguercontrol.business.model.Product
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface ProductRepository {

    fun create(product: Product): Product

    fun delete(product: Product)

    fun findById(id: UUID): Product?

    fun findByType(type: String): List<Product>

    fun findAll(pageable: Pageable): Page<Product>
}
