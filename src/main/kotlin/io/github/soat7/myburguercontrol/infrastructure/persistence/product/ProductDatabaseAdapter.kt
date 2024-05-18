package io.github.soat7.myburguercontrol.infrastructure.persistence.product

import io.github.soat7.myburguercontrol.application.ports.outbound.ProductDatabasePort
import io.github.soat7.myburguercontrol.domain.mapper.toDomain
import io.github.soat7.myburguercontrol.domain.mapper.toPersistence
import io.github.soat7.myburguercontrol.domain.model.Product
import io.github.soat7.myburguercontrol.infrastructure.persistence.product.repository.ProductRepository
import mu.KLogging
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class ProductDatabaseAdapter(
    private val repository: ProductRepository
) : ProductDatabasePort {

    private companion object : KLogging()

    override fun create(product: Product): Product = try {
        repository
            .save(product.toPersistence()).toDomain()
    } catch (ex: Exception) {
        logger.error(ex) { "An error occurred while creating new product: ${ex.message}" }
        throw ex
    }

    override fun findById(id: UUID): Product? = repository.findByIdOrNull(id)?.toDomain()

    override fun findAll(pageable: Pageable): Page<Product> = repository.findAll(pageable)
        .map { it.toDomain() }
}
