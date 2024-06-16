package io.github.soat7.myburguercontrol.database.product

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.soat7.myburguercontrol.business.mapper.toDomain
import io.github.soat7.myburguercontrol.business.mapper.toPersistence
import io.github.soat7.myburguercontrol.business.model.Product
import io.github.soat7.myburguercontrol.business.repository.ProductRepository
import io.github.soat7.myburguercontrol.database.product.repository.ProductJpaRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.util.UUID

private val logger = KotlinLogging.logger {}

@Component
class ProductGateway(
    private val repository: ProductJpaRepository,
) : ProductRepository {

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
