package io.github.soat7.myburguercontrol.application.ports.inbound

import io.github.soat7.myburguercontrol.domain.model.Product
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface ProductServicePort {

    fun create(product: Product): Product

    fun findAll(pageable: Pageable): Page<Product>

    fun findById(id: UUID): Product?
}
