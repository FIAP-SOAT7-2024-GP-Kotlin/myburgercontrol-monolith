package io.github.soat7.myburguercontrol.infrastructure.rest

import io.github.soat7.myburguercontrol.base.BaseIntegrationTest
import io.github.soat7.myburguercontrol.domain.enum.ProductType
import io.github.soat7.myburguercontrol.fixtures.ProductFixtures
import io.github.soat7.myburguercontrol.infrastructure.persistence.product.entity.ProductEntity
import io.github.soat7.myburguercontrol.infrastructure.rest.common.PaginatedResponse
import io.github.soat7.myburguercontrol.infrastructure.rest.product.api.ProductResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import java.util.UUID

class ProductIT : BaseIntegrationTest() {

    @Test
    fun `should successfully create a new product`() {
        val inputProductData = ProductFixtures.mockProductCreationRequest()

        val httpEntity = HttpEntity<Any>(inputProductData, buildAuthentication())
        val response = restTemplate.exchange(
            "/products",
            HttpMethod.POST,
            httpEntity,
            ProductResponse::class.java
        )

        assertAll(
            Executable { assertTrue(response.statusCode.is2xxSuccessful) },
            Executable { assertThat(response.body).isNotNull },
            Executable { assertEquals(inputProductData.type.name, response.body!!.type) }
        )

        val savedProduct = productRepository.findByIdOrNull(response.body!!.id)

        assertAll(
            Executable { assertThat(savedProduct).isNotNull },
            Executable { assertEquals(inputProductData.description, savedProduct!!.description) },
            Executable { assertEquals(inputProductData.price, savedProduct!!.price) }
        )
    }

    @Test
    fun `should successfully find an product by id`() {
        val id = UUID.randomUUID()
        val product = productRepository.save(ProductFixtures.mockProductEntity(id))

        val httpEntity = HttpEntity<Any>(buildAuthentication())

        val response = restTemplate.getForEntity<ProductResponse>(
            "/products/{id}",
            uriVariables = mapOf(
                "id" to product.id
            )
        )

        assertAll(
            Executable { assertTrue(response.statusCode.is2xxSuccessful) },
            Executable { assertThat(response.body).isNotNull },
            Executable { assertEquals(product.id, response.body!!.id) },
            Executable { assertEquals(product.description, response.body!!.description) },
            Executable { assertEquals(product.price, response.body!!.price) },
            Executable { assertEquals(product.type, response.body!!.type) }
        )
    }

    @Test
    fun `should return NOT_FOUND when no product is found for the given Id`() {
        val randomId = UUID.randomUUID()

        val response = restTemplate.getForEntity<ProductResponse>(
            url = "/products/{id}",
            uriVariables = mapOf(
                "id" to randomId.toString()
            )
        )
        assertEquals(response.statusCode.value(), HttpStatus.NOT_FOUND.value())
    }

    @Test
    fun `should return an empty Page of Product when no product is found`() {
        val response = restTemplate.getForEntity<PaginatedResponse<ProductResponse>>(url = "/products")

        println(response)
        assertAll(
            Executable { assertTrue(response.statusCode.is2xxSuccessful) },
            Executable { assertThat(response.body).isNotNull },
            Executable { assertThat(response.body!!.content.isEmpty()) }
        )
    }

    @Test
    fun `should return a Paginated response of product`() {
        insertRandomTypeProducts()

        val response = restTemplate.getForEntity<PaginatedResponse<ProductResponse>>(url = "/products")

        assertAll(
            Executable { assertTrue(response.statusCode.is2xxSuccessful) },
            Executable { assertThat(response.body).isNotNull },
            Executable { assertThat(response.body!!.content.isNotEmpty()) },
            Executable { assertEquals(1, response.body!!.totalPages) }
        )
    }

    private fun insertRandomTypeProducts(): ProductEntity {
        productRepository.save(ProductFixtures.mockProductEntity())
        productRepository.save(ProductFixtures.mockProductEntity(type = ProductType.DRINK))
        productRepository.save(ProductFixtures.mockProductEntity(type = ProductType.APPETIZER))
        productRepository.save(ProductFixtures.mockProductEntity(type = ProductType.DESSERT))
        return productRepository.save(ProductFixtures.mockProductEntity(type = ProductType.OTHER))
    }
}
