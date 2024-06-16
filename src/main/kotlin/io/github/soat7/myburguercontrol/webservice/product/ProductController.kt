package io.github.soat7.myburguercontrol.webservice.product

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.soat7.myburguercontrol.business.mapper.toDomain
import io.github.soat7.myburguercontrol.business.mapper.toResponse
import io.github.soat7.myburguercontrol.business.service.ProductService
import io.github.soat7.myburguercontrol.webservice.common.PaginatedResponse
import io.github.soat7.myburguercontrol.webservice.product.api.ProductCreationRequest
import io.github.soat7.myburguercontrol.webservice.product.api.ProductResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

private val logger = KotlinLogging.logger {}

@RestController("product-controller")
@RequestMapping(
    path = ["products"],
    produces = [MediaType.APPLICATION_JSON_VALUE],
)
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
@SecurityRequirement(name = "Bearer Authentication")
class ProductController(
    private val service: ProductService,
) {

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(
        tags = ["99 - Adminstrativo"],
        summary = "Utilize esta rota para cadastrar um novo produto",
        description = "Utilize esta rota para cadastrar um novo produto",
    )
    fun createProduct(@RequestBody request: ProductCreationRequest): ResponseEntity<ProductResponse> = run {
        logger.debug { "Creating product" }
        val response = service.create(request.toDomain())

        ResponseEntity.ok(response.toResponse())
    }

    @DeleteMapping(path = ["/{id}"])
    @Operation(
        tags = ["99 - Adminstrativo"],
        summary = "Utilize esta rota para apagar um produto utilizando o identificador na base de dados",
        description = "Utilize esta rota para apagar um produto utilizando o identificador na base de dados",
    )
    fun deleteProduct(@PathVariable("id") id: UUID): ResponseEntity<Void> = run {
        logger.debug { "Deleting product by id: [$id]" }
        service.delete(id)
        ResponseEntity.ok().build()
    }

    @GetMapping(path = ["/{id}"])
    @Operation(
        tags = ["99 - Adminstrativo"],
        summary = "Utilize esta rota para encontrar um produto utilizando o identificador na base de dados",
        description = "Utilize esta rota para encontrar um produto utilizando o identificador na base de dados",
    )
    fun getProductById(@PathVariable("id") id: UUID): ResponseEntity<ProductResponse> = run {
        logger.debug { "Getting product by id: [$id]" }
        service.findById(id)?.let {
            ResponseEntity.ok(it.toResponse())
        } ?: ResponseEntity.notFound().build()
    }

    @GetMapping("/type")
    @Operation(
        tags = ["2 - Jornada do Pedido"],
        summary = "Utilize esta rota para buscar todos os produtos cadastrados por categoria",
        description = "Utilize esta rota para buscar todos os produtos cadastrados por categoria",
    )
    fun getProductByType(@RequestParam type: String): ResponseEntity<List<ProductResponse>> = run {
        logger.debug { "Getting product by type: [$type]" }
        val products = service.findByType(type).map { it.toResponse() }
        ResponseEntity.ok(products)
    }

    @GetMapping
    @Operation(
        tags = ["2 - Jornada do Pedido"],
        summary = "Utilize esta rota para buscar todos os produtos cadastrados",
        description = "Utilize esta rota para buscar todos os produtos cadastrados",
    )
    fun findAll(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
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
                pageSize = response.size,
            ),
        )
    }
}
