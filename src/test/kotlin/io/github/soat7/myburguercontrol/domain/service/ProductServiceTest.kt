package io.github.soat7.myburguercontrol.domain.service

import io.github.soat7.myburguercontrol.application.ports.outbound.ProductDatabasePort
import io.github.soat7.myburguercontrol.domain.exception.ReasonCodeException
import io.github.soat7.myburguercontrol.domain.model.Product
import io.github.soat7.myburguercontrol.fixtures.ProductFixtures.mockDomainProduct
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.function.Executable
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.assertNull

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductServiceTest {

    private val databasePort = mockk<ProductDatabasePort>()
    private val service = ProductService(databasePort)

    @BeforeTest
    fun setUp() {
        clearMocks(databasePort)
    }

    @Test
    fun `should successfully create an product`() {
        val product = mockDomainProduct(id = UUID.randomUUID(), description = "Product")

        every { databasePort.create(any<Product>()) } returns product

        val result = assertDoesNotThrow {
            service.create(product)
        }

        assertNotNull(result)
        assertEquals(product, result)

        verify(exactly = 1) { databasePort.create(any<Product>()) }
    }

    @Test
    fun `should find a product by id`() {
        val id = UUID.randomUUID()
        val product = mockDomainProduct(id = id, description = "Product")

        every { databasePort.findById(any()) } returns product

        val result = assertDoesNotThrow {
            service.findById(id)
        }

        assertNotNull(result)
        assertEquals(product, result)

        verify(exactly = 1) { databasePort.findById(any()) }
    }

    @Test
    fun `should return null when no product is found by given id`() {
        val id = UUID.randomUUID()

        every { databasePort.findById(any()) } returns null

        val result = assertDoesNotThrow {
            service.findById(id)
        }

        assertNull(result)

        verify(exactly = 1) { databasePort.findById(any()) }
    }

    @Test
    fun `should throw ReasonCodeException when an exception is thrown while finding product by id`() {
        val id = UUID.randomUUID()

        every { databasePort.findById(any()) } throws Exception("Unexpected error occurred")

        val result = assertThrows(ReasonCodeException::class.java) {
            service.findById(id)
        }

        assertAll(
            Executable { assertThat(result is ReasonCodeException) },
            Executable { assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.reasonCode.status) }
        )

        verify(exactly = 1) { databasePort.findById(any()) }
    }

    @Test
    fun `should find all products`() {
        val pageable = Pageable.unpaged()
        val products = listOf(
            mockDomainProduct(id = UUID.randomUUID(), description = "Product 1"),
            mockDomainProduct(id = UUID.randomUUID(), description = "Product 2")
        )

        val page = PageImpl(products, pageable, products.size.toLong())
        every { databasePort.findAll(any()) } returns page

        val result = assertDoesNotThrow {
            service.findAll(pageable)
        }

        assertNotNull(result)
        assertEquals(page, result)

        verify(exactly = 1) { databasePort.findAll(any()) }
    }

    @Test
    fun `should throw ReasonCodeException when Exception is thrown while finding all products`() {
        val pageable = Pageable.unpaged()

        every { databasePort.findAll(any()) } throws Exception("Unexpected error occurred while finding all products")

        val result = assertThrows(ReasonCodeException::class.java) {
            service.findAll(pageable)
        }

        assertAll(
            Executable { assertThat(result is ReasonCodeException) },
            Executable { assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.reasonCode.status) }
        )

        verify(exactly = 1) { databasePort.findAll(any()) }
    }

    @Test
    fun `should return empty page when no products are found`() {
        val pageable = Pageable.unpaged()

        every { databasePort.findAll(any()) } returns Page.empty()

        val result = assertDoesNotThrow {
            service.findAll(pageable)
        }

        assertNotNull(result)
        assertThat(result).isEmpty()

        verify(exactly = 1) { databasePort.findAll(any()) }
    }
}
