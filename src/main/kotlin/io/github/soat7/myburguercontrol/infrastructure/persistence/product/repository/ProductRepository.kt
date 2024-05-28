package io.github.soat7.myburguercontrol.infrastructure.persistence.product.repository

import io.github.soat7.myburguercontrol.infrastructure.persistence.product.entity.ProductEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ProductRepository : JpaRepository<ProductEntity, UUID> {

    override fun findAll(pageable: Pageable): Page<ProductEntity>

    fun findByType(type: String): List<ProductEntity>
}
