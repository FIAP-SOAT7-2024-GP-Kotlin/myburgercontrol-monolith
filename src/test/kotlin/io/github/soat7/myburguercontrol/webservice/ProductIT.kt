package io.github.soat7.myburguercontrol.webservice

import io.github.soat7.myburguercontrol.base.BaseIntegrationTest
import io.github.soat7.myburguercontrol.business.enum.ProductType
import io.github.soat7.myburguercontrol.fixtures.ProductFixtures
import io.github.soat7.myburguercontrol.webservice.common.PaginatedResponse
import io.github.soat7.myburguercontrol.webservice.product.api.ProductResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.boot.test.web.client.exchange
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import java.util.UUID

class ProductIT : BaseIntegrationTest() {

    @Test
    fun `should successfully create a new product`() {
        val inputProductData = ProductFixtures.mockProductCreationRequest()

        val response = restTemplate.exchange<ProductResponse>(
            url = "/products",
            method = HttpMethod.POST,
            requestEntity = HttpEntity(inputProductData, authenticationHeader),
        )

        assertAll(
            Executable { assertTrue(response.statusCode.is2xxSuccessful) },
            Executable { assertThat(response.body).isNotNull },
            Executable { assertEquals(inputProductData.type.name, response.body!!.type) },
        )

        val savedProduct = productJpaRepository.findByIdOrNull(response.body!!.id)

        assertAll(
            Executable { assertThat(savedProduct).isNotNull },
            Executable { assertEquals(inputProductData.description, savedProduct!!.description) },
            Executable { assertEquals(inputProductData.price, savedProduct!!.price) },
        )
    }

    @Test
    fun `should successfully find a product by id`() {
        val id = UUID.randomUUID()
        val product = productJpaRepository.save(ProductFixtures.mockProductEntity(id))

        val response = restTemplate.exchange<ProductResponse>(
            url = "/products/{id}",
            method = HttpMethod.GET,
            requestEntity = HttpEntity(null, authenticationHeader),
            uriVariables = mapOf("id" to product.id),
        )

        assertAll(
            Executable { assertTrue(response.statusCode.is2xxSuccessful) },
            Executable { assertThat(response.body).isNotNull },
            Executable { assertEquals(product.id, response.body!!.id) },
            Executable { assertEquals(product.description, response.body!!.description) },
            Executable { assertEquals(product.price, response.body!!.price) },
            Executable { assertEquals(product.type, response.body!!.type) },
        )
    }

    @Test
    fun `should return NOT_FOUND when no product is found for the given Id`() {
        val randomId = UUID.randomUUID()

        val response = restTemplate.exchange<ProductResponse>(
            url = "/products/{id}",
            method = HttpMethod.GET,
            requestEntity = HttpEntity(null, authenticationHeader),
            uriVariables = mapOf(
                "id" to randomId.toString(),
            ),
        )
        assertEquals(response.statusCode.value(), HttpStatus.NOT_FOUND.value())
    }

    @Test
    fun `should return an empty Page of Product when no product is found`() {
        val response =
            restTemplate.exchange<PaginatedResponse<ProductResponse>>(
                url = "/products",
                method = HttpMethod.GET,
                requestEntity = HttpEntity(null, authenticationHeader),
            )

        assertAll(
            Executable { assertTrue(response.statusCode.is2xxSuccessful) },
            Executable { assertThat(response.body).isNotNull },
            Executable { assertThat(response.body!!.content.isEmpty()) },
        )
    }

    @Test
    fun `should return a Paginated response of product`() {
        insertRandomTypeProducts()

        val response = restTemplate.exchange<PaginatedResponse<ProductResponse>>(
            url = "/products",
            method = HttpMethod.GET,
            requestEntity = HttpEntity(null, authenticationHeader),
        )

        assertAll(
            Executable { assertTrue(response.statusCode.is2xxSuccessful) },
            Executable { assertThat(response.body).isNotNull },
            Executable { assertThat(response.body!!.content.isNotEmpty()) },
            Executable { assertEquals(3, response.body!!.totalPages) },
        )
    }

    @Test
    fun `should successfully find a product by type`() {
        insertRandomTypeProducts()
        val type = "DRINK"

        val response = restTemplate.exchange<List<ProductResponse>>(
            url = "/products/type?type={type}",
            method = HttpMethod.GET,
            requestEntity = HttpEntity(null, authenticationHeader),
            uriVariables = mapOf("type" to type),
        )

        assertAll(
            Executable { assertTrue(response.statusCode.is2xxSuccessful) },
            Executable { assertThat(response.body).isNotNull },
            Executable { assertThat(response.body!!).allSatisfy { it.type == type } },
        )
    }

    @Test
    fun `should return an empty product page when no products are found by type`() {
        insertRandomTypeProducts()
        val type = "PIZZA"

        val response = restTemplate.exchange<List<ProductResponse>>(
            url = "/products/type?type={type}",
            method = HttpMethod.GET,
            requestEntity = HttpEntity(null, authenticationHeader),
            uriVariables = mapOf("type" to type),
        )

        assertAll(
            Executable { assertTrue(response.statusCode.is2xxSuccessful) },
            Executable { assertThat(response.body).isEmpty() },
        )
    }

    @Test
    fun `should delete a product with given ID`() {
        val id = UUID.randomUUID()
        val product = productJpaRepository.save(ProductFixtures.mockProductEntity(id))

        val response = restTemplate.exchange<Void>(
            url = "/products/{id}",
            method = HttpMethod.DELETE,
            requestEntity = HttpEntity(null, authenticationHeader),
            uriVariables = mapOf("id" to product.id),
        )

        assertTrue(response.statusCode.is2xxSuccessful)
    }

    @Test
    fun `should return NOT_FOUND when trying to delete a product with the given Id`() {
        val randomId = UUID.randomUUID()

        val response = restTemplate.exchange<Void>(
            url = "/products/{id}",
            method = HttpMethod.DELETE,
            requestEntity = HttpEntity(null, authenticationHeader),
            uriVariables = mapOf(
                "id" to randomId.toString(),
            ),
        )

        assertEquals(response.statusCode.value(), HttpStatus.NOT_FOUND.value())
    }

    private fun insertRandomTypeProducts() {
        productJpaRepository.save(ProductFixtures.mockProductEntity())
        productJpaRepository.save(ProductFixtures.mockProductEntity(type = ProductType.DRINK))
        productJpaRepository.save(ProductFixtures.mockProductEntity(type = ProductType.APPETIZER))
        productJpaRepository.save(ProductFixtures.mockProductEntity(type = ProductType.DESSERT))
        productJpaRepository.save(ProductFixtures.mockProductEntity(type = ProductType.OTHER))
    }
}
