package io.github.soat7.myburguercontrol.domain.service

import io.github.soat7.myburguercontrol.application.ports.inbound.ProductServicePort
import io.github.soat7.myburguercontrol.application.ports.outbound.ProductDatabasePort
import io.github.soat7.myburguercontrol.domain.model.Product
import mu.KLogging
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

class ProductService(
    private val databasePort: ProductDatabasePort
) : ProductServicePort {

    private companion object : KLogging()

    override fun create(product: Product): Product {
        logger.debug { "Saving new product $product" }
        return databasePort.create(product)
    }

    override fun findAll(pageable: Pageable): Page<Product> {
        logger.debug { "Finding all products" }
        return databasePort.findAll(pageable)
    }

    override fun findById(id: UUID): Product? {
        logger.debug { "Finding product with id $id" }
        return databasePort.findById(id)
    }
}
