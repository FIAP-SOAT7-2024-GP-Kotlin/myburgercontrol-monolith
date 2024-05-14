package io.github.soat7.myburguercontrol.infrastructure.rest

import io.github.soat7.myburguercontrol.base.BaseIntegrationTest
import io.github.soat7.myburguercontrol.domain.model.ItemType
import io.github.soat7.myburguercontrol.fixtures.ItemFixtures
import io.github.soat7.myburguercontrol.infrastructure.persistence.item.repository.ItemRepository
import io.github.soat7.myburguercontrol.infrastructure.rest.common.PaginatedResponse
import io.github.soat7.myburguercontrol.infrastructure.rest.item.api.ItemResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import java.util.UUID

class ItemIT : BaseIntegrationTest() {

    @Autowired
    private lateinit var itemRepository: ItemRepository

    @Test
    fun `should successfully create a new item`() {
        val inputItemData = ItemFixtures.mockItemCreationRequest()

        val response = restTemplate.postForEntity<ItemResponse>(
            "/items",
            inputItemData
        )

        assertAll(
            Executable { assertTrue(response.statusCode.is2xxSuccessful) },
            Executable { assertThat(response.body).isNotNull },
            Executable { assertEquals(inputItemData.type.name, response.body!!.type) }
        )

        val savedItem = itemRepository.findByIdOrNull(response.body!!.id)

        assertAll(
            Executable { assertThat(savedItem).isNotNull },
            Executable { assertEquals(inputItemData.description, savedItem!!.description) },
            Executable { assertEquals(inputItemData.price, savedItem!!.price) }
        )
    }

    @Test
    fun `should successfully find an item by id`() {
        val id = UUID.randomUUID()
        val item = itemRepository.save(ItemFixtures.mockItemEntity(id))

        val response = restTemplate.getForEntity<ItemResponse>(
            "/items/{id}",
            uriVariables = mapOf(
                "id" to item.id
            )
        )

        assertAll(
            Executable { assertTrue(response.statusCode.is2xxSuccessful) },
            Executable { assertThat(response.body).isNotNull },
            Executable { assertEquals(item.id, response.body!!.id) },
            Executable { assertEquals(item.description, response.body!!.description) },
            Executable { assertEquals(item.price, response.body!!.price) },
            Executable { assertEquals(item.type, response.body!!.type) }
        )
    }

    @Test
    fun `should return NOT_FOUND when no item is found for the given Id`() {
        val randomId = UUID.randomUUID()

        val response = restTemplate.getForEntity<ItemResponse>(
            url = "/items/{id}",
            uriVariables = mapOf(
                "id" to randomId.toString()
            )
        )
        assertEquals(response.statusCode.value(), HttpStatus.NOT_FOUND.value())
    }

    @Test
    fun `should return an empty Page of Item when no item is found`() {
        val response = restTemplate.getForEntity<PaginatedResponse<ItemResponse>>(url = "/items")

        println(response)
        assertAll(
            Executable { assertTrue(response.statusCode.is2xxSuccessful) },
            Executable { assertThat(response.body).isNotNull },
            Executable { assertThat(response.body!!.content.isEmpty()) }
        )
    }

    @Test
    fun `should return a Paginated response of Item`() {
        run {
            itemRepository.save(ItemFixtures.mockItemEntity())
            itemRepository.save(ItemFixtures.mockItemEntity(type = ItemType.DRINK))
            itemRepository.save(ItemFixtures.mockItemEntity(type = ItemType.APPETIZER))
            itemRepository.save(ItemFixtures.mockItemEntity(type = ItemType.DESSERT))
            itemRepository.save(ItemFixtures.mockItemEntity(type = ItemType.OTHER))
        }

        val response = restTemplate.getForEntity<PaginatedResponse<ItemResponse>>(url = "/items")

        assertAll(
            Executable { assertTrue(response.statusCode.is2xxSuccessful) },
            Executable { assertThat(response.body).isNotNull },
            Executable { assertThat(response.body!!.content.isNotEmpty()) },
            Executable { assertEquals(1, response.body!!.totalPages) }
        )
    }
}
