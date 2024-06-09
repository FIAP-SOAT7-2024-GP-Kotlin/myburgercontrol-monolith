package io.github.soat7.myburguercontrol.database.product

import io.github.soat7.myburguercontrol.business.gateway.ProductRepository
import io.github.soat7.myburguercontrol.business.mapper.toDomain
import io.github.soat7.myburguercontrol.business.mapper.toPersistence
import io.github.soat7.myburguercontrol.business.model.Product
import io.github.soat7.myburguercontrol.database.product.repository.ProductRepository
import mu.KLogging
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class ProductDatabaseAdapter(
    private val repository: ProductRepository
) : io.github.soat7.myburguercontrol.business.gateway.ProductRepository {

    private companion object : KLogging()

    override fun create(product: Product): Product = try {
        repository
            .save(product.toPersistence()).toDomain()
    } catch (ex: Exception) {
        logger.error(ex) { "An error occurred while creating new product: ${ex.message}" }
        throw ex
    }

    override fun delete(product: Product) {
        repository.deleteById(product.id)
    }

    override fun findById(id: UUID): Product? = repository.findByIdOrNull(id)?.toDomain()

    override fun findByType(type: String): List<Product> = repository.findByType(type)
        .map { it.toDomain() }

    override fun findAll(pageable: Pageable): Page<Product> = repository.findAll(pageable)
        .map { it.toDomain() }
}
