package io.github.soat7.myburguercontrol.domain.service

import io.github.soat7.myburguercontrol.application.ports.outbound.CustomerDatabasePort
import io.github.soat7.myburguercontrol.application.ports.outbound.OrderDatabasePort
import io.github.soat7.myburguercontrol.domain.enum.OrderStatus
import io.github.soat7.myburguercontrol.domain.model.Customer
import io.github.soat7.myburguercontrol.domain.model.Order
import io.github.soat7.myburguercontrol.fixtures.CustomerFixtures.mockDomainCustomer
import io.github.soat7.myburguercontrol.fixtures.OrderFixtures.mockOrder
import io.github.soat7.myburguercontrol.util.toBigDecimal
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class OrderServiceTest {

    private val orderDatabasePort = mockk<OrderDatabasePort>()
    private val customerDatabasePort = mockk<CustomerDatabasePort>()
    private val service = OrderService(orderDatabasePort, customerDatabasePort)

    @BeforeTest
    fun setUp() {
        clearMocks(customerDatabasePort)
        clearMocks(orderDatabasePort)
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    fun `should create a new order using cpf`() {
        // Given
        val cpf = "23282711034"
        val customer = mockDomainCustomer(cpf = cpf)

        every { customerDatabasePort.findCustomerByCpf(cpf) } returns customer
        every { orderDatabasePort.create(any<Order>()) } answers {
            (this.firstArg() as Order).copy(id = UUID.randomUUID())
        }

        // When
        val order = service.newOrder(cpf)

        // Then
        verify(exactly = 1) { customerDatabasePort.findCustomerByCpf(any()) }
        verify(exactly = 1) { orderDatabasePort.create(any()) }

        assertNotNull(order.id)
        assertEquals(cpf, order.customer.cpf)
        assertEquals(OrderStatus.NEW, order.status)
        assertTrue(order.items.isEmpty())
        assertEquals(0.0.toBigDecimal(), order.total)
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    fun `should create a new order using id`() {
        // Given
        val customerId = UUID.randomUUID()
        val cpf = "23282711034"
        val customer = mockDomainCustomer(id = customerId, cpf = cpf)

        every { customerDatabasePort.findCustomerById(customerId) } returns customer
        every { orderDatabasePort.create(any<Order>()) } answers {
            (this.firstArg() as Order).copy(id = UUID.randomUUID())
        }

        // When
        val order = service.newOrder(customerId)

        // Then
        verify(exactly = 1) { customerDatabasePort.findCustomerById(any()) }
        verify(exactly = 1) { orderDatabasePort.create(any()) }

        assertNotNull(order.id)
        assertEquals(customerId, order.customer.id)
        assertEquals(cpf, order.customer.cpf)
        assertEquals(OrderStatus.NEW, order.status)
        assertTrue(order.items.isEmpty())
        assertEquals(0.0.toBigDecimal(), order.total)
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    fun `should get user orders using id`() {
        // Given
        val customerId = UUID.randomUUID()
        val cpf = "23282711034"
        val customer = mockDomainCustomer(id = customerId, cpf = cpf)

        every { customerDatabasePort.findCustomerById(customerId) } returns customer
        every { orderDatabasePort.findByCustomer(any<Customer>()) } returns listOf(
            mockOrder(customer = customer)
        )

        // When
        val orders = service.findOrders(customerId)

        // Then
        verify(exactly = 1) { customerDatabasePort.findCustomerById(any()) }
        verify(exactly = 1) { orderDatabasePort.findByCustomer(any()) }

        assertFalse(orders.isEmpty())

        val order = orders.first()

        assertEquals(customerId, order.customer.id)
        assertEquals(cpf, order.customer.cpf)
        assertEquals(OrderStatus.NEW, order.status)
        assertFalse(order.items.isEmpty())
        assertEquals(1.0.toBigDecimal(), order.total)
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    fun `should get user orders using cpf`() {
        // Given
        val customerId = UUID.randomUUID()
        val cpf = "23282711034"
        val customer = mockDomainCustomer(id = customerId, cpf = cpf)

        every { customerDatabasePort.findCustomerByCpf(cpf) } returns customer
        every { orderDatabasePort.findByCustomer(any<Customer>()) } returns listOf(
            mockOrder(customer = customer)
        )

        // When
        val orders = service.findOrders(cpf)

        // Then
        verify(exactly = 1) { customerDatabasePort.findCustomerByCpf(any()) }
        verify(exactly = 1) { orderDatabasePort.findByCustomer(any()) }

        assertFalse(orders.isEmpty())

        val order = orders.first()

        assertEquals(customerId, order.customer.id)
        assertEquals(cpf, order.customer.cpf)
        assertEquals(OrderStatus.NEW, order.status)
        assertFalse(order.items.isEmpty())
        assertEquals(1.0.toBigDecimal(), order.total)
    }
}
