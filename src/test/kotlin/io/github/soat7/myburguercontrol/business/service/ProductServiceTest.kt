package io.github.soat7.myburguercontrol.business.service

import io.github.soat7.myburguercontrol.business.exception.ReasonCodeException
import io.github.soat7.myburguercontrol.business.model.Product
import io.github.soat7.myburguercontrol.business.repository.ProductRepository
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

    private val repository = mockk<ProductRepository>()
    private val service = ProductService(repository)

    @BeforeTest
    fun setUp() {
        clearMocks(repository)
    }

    @Test
    fun `should successfully create an product`() {
        val product = mockDomainProduct(id = UUID.randomUUID(), description = "Product")

        every { repository.create(any<Product>()) } returns product

        val result = assertDoesNotThrow {
            service.create(product)
        }

        assertNotNull(result)
        assertEquals(product, result)

        verify(exactly = 1) { repository.create(any<Product>()) }
    }

    @Test
    fun `should find a product by id`() {
        val id = UUID.randomUUID()
        val product = mockDomainProduct(id = id, description = "Product")

        every { repository.findById(any()) } returns product

        val result = assertDoesNotThrow {
            service.findById(id)
        }

        assertNotNull(result)
        assertEquals(product, result)

        verify(exactly = 1) { repository.findById(any()) }
    }

    @Test
    fun `should return null when no product is found by given id`() {
        val id = UUID.randomUUID()

        every { repository.findById(any()) } returns null

        val result = assertDoesNotThrow {
            service.findById(id)
        }

        assertNull(result)

        verify(exactly = 1) { repository.findById(any()) }
    }

    @Test
    fun `should throw ReasonCodeException when an exception is thrown while finding product by id`() {
        val id = UUID.randomUUID()

        every { repository.findById(any()) } throws Exception("Unexpected error occurred")

        val result = assertThrows(ReasonCodeException::class.java) {
            service.findById(id)
        }

        assertAll(
            Executable { assertThat(result is ReasonCodeException) },
            Executable { assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.reasonCode.status) },
        )

        verify(exactly = 1) { repository.findById(any()) }
    }

    @Test
    fun `should find all products`() {
        val pageable = Pageable.unpaged()
        val products = listOf(
            mockDomainProduct(id = UUID.randomUUID(), description = "Product 1"),
            mockDomainProduct(id = UUID.randomUUID(), description = "Product 2"),
        )

        val page = PageImpl(products, pageable, products.size.toLong())
        every { repository.findAll(any()) } returns page

        val result = assertDoesNotThrow {
            service.findAll(pageable)
        }

        assertNotNull(result)
        assertEquals(page, result)

        verify(exactly = 1) { repository.findAll(any()) }
    }

    @Test
    fun `should throw ReasonCodeException when Exception is thrown while finding all products`() {
        val pageable = Pageable.unpaged()

        every { repository.findAll(any()) } throws Exception("Unexpected error occurred while finding all products")

        val result = assertThrows(ReasonCodeException::class.java) {
            service.findAll(pageable)
        }

        assertAll(
            Executable { assertThat(result is ReasonCodeException) },
            Executable { assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.reasonCode.status) },
        )

        verify(exactly = 1) { repository.findAll(any()) }
    }

    @Test
    fun `should return empty page when no products are found`() {
        val pageable = Pageable.unpaged()

        every { repository.findAll(any()) } returns Page.empty()

        val result = assertDoesNotThrow {
            service.findAll(pageable)
        }

        assertNotNull(result)
        assertThat(result).isEmpty()

        verify(exactly = 1) { repository.findAll(any()) }
    }
}
