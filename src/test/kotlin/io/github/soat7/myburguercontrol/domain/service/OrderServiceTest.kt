package io.github.soat7.myburguercontrol.domain.service

import io.github.soat7.myburguercontrol.application.ports.inbound.CustomerServicePort
import io.github.soat7.myburguercontrol.application.ports.inbound.PaymentServicePort
import io.github.soat7.myburguercontrol.application.ports.inbound.ProductServicePort
import io.github.soat7.myburguercontrol.application.ports.outbound.OrderDatabasePort
import io.github.soat7.myburguercontrol.domain.enum.OrderStatus
import io.github.soat7.myburguercontrol.fixtures.CustomerFixtures.mockDomainCustomer
import io.github.soat7.myburguercontrol.fixtures.OrderDetailFixtures
import io.github.soat7.myburguercontrol.fixtures.PaymentFixtures.mockPayment
import io.github.soat7.myburguercontrol.fixtures.ProductFixtures
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
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import io.github.soat7.myburguercontrol.domain.model.Order as OrderModel

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class OrderServiceTest {

    private val orderDatabasePort = mockk<OrderDatabasePort>()
    private val customerServicePort = mockk<CustomerServicePort>()
    private val productServicePort = mockk<ProductServicePort>()
    private val paymentServicePort = mockk<PaymentServicePort>()
    private val service = OrderService(orderDatabasePort, customerServicePort, productServicePort, paymentServicePort)

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
        val product = ProductFixtures.mockDomainProduct()
        val payment = mockPayment()

        every { customerServicePort.findCustomerByCpf(cpf) } returns customer
        every { orderDatabasePort.create(any<OrderModel>()) } answers {
            (this.firstArg() as OrderModel).copy(id = UUID.randomUUID())
        }
        every { productServicePort.findById(any()) } returns product
        every {
            paymentServicePort.requestPayment(any())
        } returns payment
        every { paymentServicePort.createPayment() } returns payment
        every { orderDatabasePort.update(any<OrderModel>()) } answers {
            (this.firstArg() as OrderModel).copy(id = UUID.randomUUID())
        }

        val order = service.createOrder(OrderDetailFixtures.mockOrderDetail(cpf = cpf, product = product))

        verify(exactly = 1) { customerServicePort.findCustomerByCpf(any()) }
        verify(exactly = 2) { orderDatabasePort.update(any()) }
        verify(exactly = 1) { orderDatabasePort.create(any()) }
        verify(exactly = 1) { paymentServicePort.createPayment() }
        verify(exactly = 1) { paymentServicePort.requestPayment(any()) }

        assertNotNull(order.id)
        assertEquals(cpf, order.customer.cpf)
        assertEquals(OrderStatus.RECEIVED, order.status)
        assertFalse(order.items.isEmpty())
        assertEquals(1.0.toBigDecimal(), order.total)
    }
}
