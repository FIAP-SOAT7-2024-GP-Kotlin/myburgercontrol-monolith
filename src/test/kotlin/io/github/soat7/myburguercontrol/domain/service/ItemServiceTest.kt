package io.github.soat7.myburguercontrol.domain.service

import io.github.soat7.myburguercontrol.application.ports.outbound.ItemDatabasePort
import io.github.soat7.myburguercontrol.domain.model.Item
import io.github.soat7.myburguercontrol.fixtures.ItemFixtures.mockDomainItem
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.assertNull

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemServiceTest {

    private val databasePort = mockk<ItemDatabasePort>()
    private val service = ItemService(databasePort)

    @BeforeTest
    fun setUp() {
        clearMocks(databasePort)
    }

    @Test
    fun `should successfully create an item`() {
        val item = mockDomainItem(id = UUID.randomUUID(), description = "Item")

        every { databasePort.create(any<Item>()) } returns item

        val result = assertDoesNotThrow {
            service.create(item)
        }

        assertNotNull(result)
        assertEquals(item, result)

        verify(exactly = 1) { databasePort.create(any<Item>()) }
    }

    @Test
    fun `should find an item by id`() {
        val id = UUID.randomUUID()
        val item = mockDomainItem(id = id, description = "Item")

        every { databasePort.findById(any()) } returns item

        val result = assertDoesNotThrow {
            service.findById(id)
        }

        assertNotNull(result)
        assertEquals(item, result)

        verify(exactly = 1) { databasePort.findById(any()) }
    }

    @Test
    fun `should return null when no Item is found by given id`() {
        val id = UUID.randomUUID()

        every { databasePort.findById(any()) } returns null

        val result = assertDoesNotThrow {
            service.findById(id)
        }

        assertNull(result)

        verify(exactly = 1) { databasePort.findById(any()) }
    }

    @Test
    fun `should find all items`() {
        val pageable = Pageable.unpaged()
        val items = listOf(
            mockDomainItem(id = UUID.randomUUID(), description = "Item 1"),
            mockDomainItem(id = UUID.randomUUID(), description = "Item 2")
        )

        val page = PageImpl(items, pageable, items.size.toLong())
        every { databasePort.findAll(any()) } returns page

        val result = assertDoesNotThrow {
            service.findAll(pageable)
        }

        assertNotNull(result)
        assertEquals(page, result)

        verify(exactly = 1) { databasePort.findAll(any()) }
    }

    @Test
    fun `should return empty page when no items are found`() {
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
