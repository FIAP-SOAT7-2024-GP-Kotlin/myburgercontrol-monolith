package io.github.soat7.myburguercontrol.base

import io.github.soat7.myburguercontrol.Application
import io.github.soat7.myburguercontrol.container.PostgresContainer
import io.github.soat7.myburguercontrol.domain.enum.UserRole
import io.github.soat7.myburguercontrol.domain.mapper.toPersistence
import io.github.soat7.myburguercontrol.domain.model.Customer
import io.github.soat7.myburguercontrol.fixtures.AuthFixtures
import io.github.soat7.myburguercontrol.fixtures.ProductFixtures
import io.github.soat7.myburguercontrol.fixtures.UserFixtures
import io.github.soat7.myburguercontrol.infrastructure.persistence.customer.entity.CustomerEntity
import io.github.soat7.myburguercontrol.infrastructure.persistence.customer.repository.CustomerRepository
import io.github.soat7.myburguercontrol.infrastructure.persistence.order.repository.OrderRepository
import io.github.soat7.myburguercontrol.infrastructure.persistence.product.entity.ProductEntity
import io.github.soat7.myburguercontrol.infrastructure.persistence.product.repository.ProductRepository
import io.github.soat7.myburguercontrol.infrastructure.persistence.user.repository.UserRepository
import io.github.soat7.myburguercontrol.infrastructure.rest.auth.api.AuthResponse
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
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ExtendWith(PostgresContainer::class)
class BaseIntegrationTest {

    @Autowired
    protected lateinit var restTemplate: TestRestTemplate

    @Autowired
    protected lateinit var productRepository: ProductRepository

    @Autowired
    protected lateinit var customerRepository: CustomerRepository

    @Autowired
    protected lateinit var orderRepository: OrderRepository

    @Autowired
    protected lateinit var userRepository: UserRepository

    @Autowired
    protected lateinit var passwordEncoder: PasswordEncoder

    protected lateinit var authenticationHeader: MultiValueMap<String, String>

    @BeforeEach
    fun setUpAuthentication() {
        println("Cleaning User database...")
        userRepository.deleteAll()
        authenticationHeader = buildAuthentication()
    }

    protected fun insertProducts(): List<ProductEntity> {
        productRepository.save(ProductFixtures.mockProductEntity())
        productRepository.save(ProductFixtures.mockProductEntity())

        return productRepository.findAll()
    }

    protected fun insertCustomerData(customer: Customer): CustomerEntity {
        return customerRepository.save(customer.toPersistence())
    }

    protected fun buildAuthentication(): MultiValueMap<String, String> {
        val cpf = "15666127055"
        val password = UUID.randomUUID().toString()
        val userRole = UserRole.ADMIN
        userRepository.save(
            UserFixtures.mockUserEntity(
                cpf = cpf,
                password = passwordEncoder.encode(password),
                userRole = userRole
            )
        )

        val response = restTemplate.postForEntity<AuthResponse>(
            "/auth",
            AuthFixtures.mockAuthCreationRequest(cpf, password)
        ).body
            ?: throw RuntimeException("Failed to authenticate")

        val header: MultiValueMap<String, String> = LinkedMultiValueMap()
        header.add("Authorization", "Bearer ${response.accessToken}")
        header.add("Content-Type", MediaType.APPLICATION_JSON_VALUE)

        return header
    }
}
