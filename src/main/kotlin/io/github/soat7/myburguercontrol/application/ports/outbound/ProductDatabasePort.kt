package io.github.soat7.myburguercontrol.application.ports.outbound

import io.github.soat7.myburguercontrol.domain.model.Product
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface ProductDatabasePort {

    fun create(product: Product): Product

    fun findById(id: UUID): Product?

    fun findAll(pageable: Pageable): Page<Product>
}
