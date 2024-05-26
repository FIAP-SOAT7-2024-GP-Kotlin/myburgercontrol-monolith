package io.github.soat7.myburguercontrol.infrastructure.rest.product

import io.github.soat7.myburguercontrol.application.ports.inbound.ProductServicePort
import io.github.soat7.myburguercontrol.domain.mapper.toDomain
import io.github.soat7.myburguercontrol.domain.mapper.toResponse
import io.github.soat7.myburguercontrol.infrastructure.rest.common.PaginatedResponse
import io.github.soat7.myburguercontrol.infrastructure.rest.product.api.ProductCreationRequest
import io.github.soat7.myburguercontrol.infrastructure.rest.product.api.ProductResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
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
@RequestMapping("products")
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
@SecurityRequirement(name = "Bearer Authentication")
class ProductController(
    private val service: ProductServicePort
) {

    private companion object : KLogging()

    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(
        tags = ["99 - Comum"],
        summary = "Utilize esta rota para cadastrar um novo produto",
        description = "Utilize esta rota para cadastrar um novo produto",
        operationId = "2"
    )
    fun createProduct(@RequestBody request: ProductCreationRequest): ResponseEntity<ProductResponse> = run {
        logger.debug { "Creating product" }
        val response = service.create(request.toDomain())

        ResponseEntity.ok(response.toResponse())
    }

    @GetMapping(
        path = ["/{id}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(
        tags = ["99 - Comum"],
        summary = "Utilize esta rota para encontrar um produto utilizando o identificador na base de dados",
        description = "Utilize esta rota para encontrar um produto utilizando o identificador na base de dados",
        operationId = "3"
    )
    fun getProductById(@PathVariable("id") id: UUID): ResponseEntity<ProductResponse> = run {
        logger.debug { "Getting product by id: [$id]" }
        service.findById(id)?.let {
            ResponseEntity.ok(it.toResponse())
        } ?: ResponseEntity.notFound().build()
    }

    @GetMapping
    @Operation(
        tags = ["2 - Jornada do Pedido"],
        summary = "Utilize esta rota para buscar todos os produtos cadastrados",
        description = "Utilize esta rota para buscar todos os produtos cadastrados",
        operationId = "1"
    )
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
