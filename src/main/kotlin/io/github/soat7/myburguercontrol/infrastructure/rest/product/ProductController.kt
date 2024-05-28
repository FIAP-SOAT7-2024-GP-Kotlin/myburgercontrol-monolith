package io.github.soat7.myburguercontrol.infrastructure.rest.product

import io.github.soat7.myburguercontrol.application.ports.inbound.ProductServicePort
import io.github.soat7.myburguercontrol.domain.mapper.toDomain
import io.github.soat7.myburguercontrol.domain.mapper.toResponse
import io.github.soat7.myburguercontrol.infrastructure.rest.common.PaginatedResponse
import io.github.soat7.myburguercontrol.infrastructure.rest.product.api.ProductCreationRequest
import io.github.soat7.myburguercontrol.infrastructure.rest.product.api.ProductResponse
import mu.KLogging
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController("product-controller")
@RequestMapping(
    path = ["products"],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
class ProductController(
    private val service: ProductServicePort
) {

    private companion object : KLogging()

    @PostMapping
    fun createProduct(@RequestBody request: ProductCreationRequest): ResponseEntity<ProductResponse> = run {
        logger.debug { "Creating product" }
        val response = service.create(request.toDomain())

        ResponseEntity.ok(response.toResponse())
    }

    @GetMapping("/{id}")
    fun getProductById(@PathVariable("id") id: UUID): ResponseEntity<ProductResponse> = run {
        logger.debug { "Getting product by id: [$id]" }
        service.findById(id)?.let {
            ResponseEntity.ok(it.toResponse())
        } ?: ResponseEntity.notFound().build()
    }

    @GetMapping("/type")
    fun getProductByType(@RequestParam type: String): ResponseEntity<List<ProductResponse>> = run {
        logger.debug { "Getting product by type: [$type]" }
        val products = service.findByType(type).map { it.toResponse() }
        if (products.isNotEmpty()) {
            ResponseEntity.ok(products)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping
    fun findAll(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<PaginatedResponse<ProductResponse>> = run {
        logger.debug { "Listing all products" }
        val pageable = PageRequest.of(page, size)

        val response = service.findAll(pageable)

        ResponseEntity.ok(
            PaginatedResponse(
                content = response.content.map { it.toResponse() },
                totalPages = response.totalPages,
                totalElements = response.totalElements,
                currentPage = response.number,
                pageSize = response.size
            )
        )
    }
}
