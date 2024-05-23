package io.github.soat7.myburguercontrol.domain.service

import io.github.soat7.myburguercontrol.application.ports.inbound.CustomerServicePort
import io.github.soat7.myburguercontrol.application.ports.inbound.ProductServicePort
import io.github.soat7.myburguercontrol.application.ports.outbound.OrderDatabasePort
import io.github.soat7.myburguercontrol.domain.enum.OrderStatus
import io.github.soat7.myburguercontrol.fixtures.CustomerFixtures.mockDomainCustomer
import io.github.soat7.myburguercontrol.fixtures.OrderDetailFixtures
import io.github.soat7.myburguercontrol.util.toBigDecimal
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import io.github.soat7.myburguercontrol.domain.model.Order as OrderModel

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class OrderServiceTest {

    private val orderDatabasePort = mockk<OrderDatabasePort>()
    private val customerServicePort = mockk<CustomerServicePort>()
    private val productServicePort = mockk<ProductServicePort>()
    private val service = OrderService(orderDatabasePort, customerServicePort, productServicePort)

    @BeforeTest
    fun setUp() {
        clearMocks(customerServicePort)
        clearMocks(orderDatabasePort)
    }

    @Test
    @Order(1)
    fun `should create a new order using cpf`() {
        val cpf = "23282711034"
        val customer = mockDomainCustomer(cpf = cpf)

        every { customerServicePort.findCustomerByCpf(cpf) } returns customer
        every { orderDatabasePort.create(any<OrderModel>()) } answers {
            (this.firstArg() as OrderModel).copy(id = UUID.randomUUID())
        }

        val order = service.createOrder(OrderDetailFixtures.mockOrderDetail(cpf = cpf))

        verify(exactly = 1) { customerServicePort.findCustomerByCpf(any()) }
        verify(exactly = 1) { orderDatabasePort.create(any()) }

        assertNotNull(order.id)
        assertEquals(cpf, order.customer.cpf)
        assertEquals(OrderStatus.NEW, order.status)
        assertTrue(order.items.isEmpty())
        assertEquals(0.0.toBigDecimal(), order.total)
    }
}
