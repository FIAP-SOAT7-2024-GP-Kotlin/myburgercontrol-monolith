package io.github.soat7.myburguercontrol.domain.service

import io.github.soat7.myburguercontrol.business.enum.OrderStatus
import io.github.soat7.myburguercontrol.business.repository.OrderRepository
import io.github.soat7.myburguercontrol.business.service.CustomerService
import io.github.soat7.myburguercontrol.business.service.OrderService
import io.github.soat7.myburguercontrol.business.service.PaymentService
import io.github.soat7.myburguercontrol.business.service.ProductService
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
import io.github.soat7.myburguercontrol.business.model.Order as OrderModel

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class OrderServiceTest {

    private val orderRepository = mockk<OrderRepository>()
    private val customerService = mockk<CustomerService>()
    private val productService = mockk<ProductService>()
    private val paymentService = mockk<PaymentService>()
    private val service = OrderService(orderRepository, customerService, productService, paymentService)

    @BeforeTest
    fun setUp() {
        clearMocks(customerService)
        clearMocks(orderRepository)
    }

    @Test
    @Order(1)
    fun `should create a new order using cpf`() {
        val cpf = "23282711034"
        val customer = mockDomainCustomer(cpf = cpf)
        val product = ProductFixtures.mockDomainProduct()
        val payment = mockPayment()

        every { customerService.findCustomerByCpf(cpf) } returns customer
        every { orderRepository.create(any<OrderModel>()) } answers {
            (this.firstArg() as OrderModel).copy(id = UUID.randomUUID())
        }
        every { productService.findById(any()) } returns product
        every {
            paymentService.requestPayment(any())
        } returns payment
        every { paymentService.createPayment() } returns payment
        every { orderRepository.update(any<OrderModel>()) } answers {
            (this.firstArg() as OrderModel).copy(id = UUID.randomUUID())
        }

        val order = service.createOrder(OrderDetailFixtures.mockOrderDetail(cpf = cpf, product = product))

        verify(exactly = 1) { customerService.findCustomerByCpf(any()) }
        verify(exactly = 2) { orderRepository.update(any()) }
        verify(exactly = 1) { orderRepository.create(any()) }
        verify(exactly = 1) { paymentService.createPayment() }
        verify(exactly = 1) { paymentService.requestPayment(any()) }

        assertNotNull(order.id)
        assertEquals(cpf, order.customer.cpf)
        assertEquals(OrderStatus.RECEIVED, order.status)
        assertFalse(order.items.isEmpty())
        assertEquals(1.0.toBigDecimal(), order.total)
    }
}
