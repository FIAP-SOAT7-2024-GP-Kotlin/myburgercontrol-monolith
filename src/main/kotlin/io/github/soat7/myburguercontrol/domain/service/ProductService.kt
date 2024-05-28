package io.github.soat7.myburguercontrol.domain.service

import io.github.soat7.myburguercontrol.application.ports.inbound.ProductServicePort
import io.github.soat7.myburguercontrol.application.ports.outbound.ProductDatabasePort
import io.github.soat7.myburguercontrol.domain.exception.ReasonCode
import io.github.soat7.myburguercontrol.domain.exception.ReasonCodeException
import io.github.soat7.myburguercontrol.domain.model.Product
import mu.KLogging
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

class ProductService(
    private val databasePort: ProductDatabasePort
) : ProductServicePort {

    private companion object : KLogging()

    override fun create(product: Product): Product = try {
        logger.debug { "Saving new product $product" }
        databasePort.create(product)
    } catch (ex: Exception) {
        logger.error(ex) { "Error while creating product" }
        throw ReasonCodeException(ReasonCode.UNEXPECTED_ERROR, ex)
    }

    override fun findAll(pageable: Pageable): Page<Product> = try {
        logger.debug { "Finding all products" }
        databasePort.findAll(pageable)
    } catch (ex: Exception) {
        logger.error(ex) { "Error while listing products" }
        throw ReasonCodeException(ReasonCode.UNEXPECTED_ERROR, ex)
    }

    override fun findById(id: UUID): Product? = try {
        logger.debug { "Finding product with id $id" }
        databasePort.findById(id)
    } catch (ex: Exception) {
        logger.error(ex) { "Error while finding product by type" }
        throw ReasonCodeException(ReasonCode.UNEXPECTED_ERROR, ex)
    }

    override fun findByType(type: String): List<Product> = try {
        logger.debug { "Finding product with type $type" }
        databasePort.findByType(type)
    } catch (ex: Exception) {
        logger.error(ex) { "Error while finding product by id" }
        throw ReasonCodeException(ReasonCode.UNEXPECTED_ERROR, ex)
    }
}
