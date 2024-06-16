package io.github.soat7.myburguercontrol.base

import io.github.soat7.myburguercontrol.Application
import io.github.soat7.myburguercontrol.business.enum.UserRole
import io.github.soat7.myburguercontrol.business.mapper.toPersistence
import io.github.soat7.myburguercontrol.business.model.Customer
import io.github.soat7.myburguercontrol.business.model.Payment
import io.github.soat7.myburguercontrol.container.MockServerContainer
import io.github.soat7.myburguercontrol.container.PostgresContainer
import io.github.soat7.myburguercontrol.database.customer.entity.CustomerEntity
import io.github.soat7.myburguercontrol.database.customer.repository.CustomerJpaRepository
import io.github.soat7.myburguercontrol.database.order.repository.OrderJpaRepository
import io.github.soat7.myburguercontrol.database.payment.repository.PaymentJpaRepository
import io.github.soat7.myburguercontrol.database.product.entity.ProductEntity
import io.github.soat7.myburguercontrol.database.product.repository.ProductJpaRepository
import io.github.soat7.myburguercontrol.database.user.repository.UserJpaRepository
import io.github.soat7.myburguercontrol.fixtures.AuthFixtures
import io.github.soat7.myburguercontrol.fixtures.ProductFixtures
import io.github.soat7.myburguercontrol.fixtures.UserFixtures
import io.github.soat7.myburguercontrol.webservice.auth.api.AuthResponse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import java.util.UUID

@ActiveProfiles("test")
@SpringBootTest(
    classes = [Application::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)
@ExtendWith(PostgresContainer::class, MockServerContainer::class)
class BaseIntegrationTest {

    @Autowired
    protected lateinit var restTemplate: TestRestTemplate

    @Autowired
    protected lateinit var productJpaRepository: ProductJpaRepository

    @Autowired
    protected lateinit var customerJpaRepository: CustomerJpaRepository

    @Autowired
    protected lateinit var orderJpaRepository: OrderJpaRepository

    @Autowired
    protected lateinit var userJpaRepository: UserJpaRepository

    @Autowired
    protected lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    protected lateinit var paymentJpaRepository: PaymentJpaRepository

    protected lateinit var authenticationHeader: MultiValueMap<String, String>

    @BeforeEach
    fun setUpAuthentication() {
        println("Cleaning User database...")
        userJpaRepository.deleteAll()
        authenticationHeader = buildAuthentication()
    }

    protected fun insertProducts(): List<ProductEntity> {
        productJpaRepository.save(ProductFixtures.mockProductEntity())
        productJpaRepository.save(ProductFixtures.mockProductEntity())

        return productJpaRepository.findAll()
    }

    protected fun insertCustomerData(customer: Customer): CustomerEntity {
        return customerJpaRepository.save(customer.toPersistence())
    }

    protected fun insertPaymentData(payment: Payment) = paymentJpaRepository.save(payment.toPersistence())

    protected fun buildAuthentication(): MultiValueMap<String, String> {
        val cpf = "15666127055"
        val password = UUID.randomUUID().toString()
        val userRole = UserRole.ADMIN
        userJpaRepository.save(
            UserFixtures.mockUserEntity(
                cpf = cpf,
                password = passwordEncoder.encode(password),
                userRole = userRole,
            ),
        )

        val response = restTemplate.postForEntity<AuthResponse>(
            "/auth",
            AuthFixtures.mockAuthCreationRequest(cpf, password),
        ).body
            ?: throw RuntimeException("Failed to authenticate")

        val header: MultiValueMap<String, String> = LinkedMultiValueMap()
        header.add("Authorization", "Bearer ${response.accessToken}")
        header.add("Content-Type", MediaType.APPLICATION_JSON_VALUE)

        return header
    }
}
