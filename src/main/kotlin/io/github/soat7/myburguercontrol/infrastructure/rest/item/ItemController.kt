package io.github.soat7.myburguercontrol.infrastructure.rest.item

import io.github.soat7.myburguercontrol.application.ports.inbound.ItemServicePort
import io.github.soat7.myburguercontrol.domain.mapper.toDomain
import io.github.soat7.myburguercontrol.domain.mapper.toResponse
import io.github.soat7.myburguercontrol.infrastructure.rest.common.PaginatedResponse
import io.github.soat7.myburguercontrol.infrastructure.rest.item.api.ItemCreationRequest
import io.github.soat7.myburguercontrol.infrastructure.rest.item.api.ItemResponse
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

@RestController("item-controller")
@RequestMapping("/api/v1/items")
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
class ItemController(
    private val service: ItemServicePort
) {

    private companion object : KLogging()

    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun createItem(@RequestBody request: ItemCreationRequest): ResponseEntity<ItemResponse> = run {
        logger.info { "Creating item" }
        val response = service.create(request.toDomain())

        ResponseEntity.ok(response.toResponse())
    }

    @GetMapping(
        path = ["/{id}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getItemById(@PathVariable("id") id: UUID) = run {
        logger.info { "Getting item by id: [$id]" }
        service.findById(id)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    @GetMapping
    fun findAll(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<PaginatedResponse<ItemResponse>> = run {
        logger.info { "Listing all items" }
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
