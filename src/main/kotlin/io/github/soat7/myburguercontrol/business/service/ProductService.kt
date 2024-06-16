package io.github.soat7.myburguercontrol.business.service

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.soat7.myburguercontrol.business.exception.ReasonCode
import io.github.soat7.myburguercontrol.business.exception.ReasonCode.PRODUCT_NOT_FOUND
import io.github.soat7.myburguercontrol.business.exception.ReasonCodeException
import io.github.soat7.myburguercontrol.business.model.Product
import io.github.soat7.myburguercontrol.business.repository.ProductRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

private val logger = KotlinLogging.logger {}

@Service
class ProductService(
    private val databasePort: ProductRepository,
) {

    fun create(product: Product): Product = try {
        logger.debug { "Saving new product $product" }
        databasePort.create(product)
    } catch (ex: Exception) {
        logger.error(ex) { "Error while creating product" }
        throw ReasonCodeException(ReasonCode.UNEXPECTED_ERROR, ex)
    }

    fun delete(id: UUID) {
        logger.debug { "Saving product with id $id" }
        val product = databasePort.findById(id) ?: throw ReasonCodeException(PRODUCT_NOT_FOUND)
        databasePort.delete(product)
    }

    fun findAll(pageable: Pageable): Page<Product> = try {
        logger.debug { "Finding all products" }
        databasePort.findAll(pageable)
    } catch (ex: Exception) {
        logger.error(ex) { "Error while listing products" }
        throw ReasonCodeException(ReasonCode.UNEXPECTED_ERROR, ex)
    }

    fun findById(id: UUID): Product? = try {
        logger.debug { "Finding product with id $id" }
        databasePort.findById(id)
    } catch (ex: Exception) {
        logger.error(ex) { "Error while finding product by type" }
        throw ReasonCodeException(ReasonCode.UNEXPECTED_ERROR, ex)
    }

    fun findByType(type: String): List<Product> = try {
        logger.debug { "Finding product with type $type" }
        databasePort.findByType(type)
    } catch (ex: Exception) {
        logger.error(ex) { "Error while finding product by id" }
        throw ReasonCodeException(ReasonCode.UNEXPECTED_ERROR, ex)
    }
}
