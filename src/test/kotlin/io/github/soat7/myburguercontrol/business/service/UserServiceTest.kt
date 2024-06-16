package io.github.soat7.myburguercontrol.business.service

import io.github.soat7.myburguercontrol.business.enum.UserRole
import io.github.soat7.myburguercontrol.business.model.User
import io.github.soat7.myburguercontrol.business.repository.UserRepository
import io.github.soat7.myburguercontrol.fixtures.UserFixtures
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class UserServiceTest {

    private val userRepository = mockk<UserRepository>()
    private val passwordEncoder: PasswordEncoder = BCryptPasswordEncoder()

    private val service = UserService(userRepository, passwordEncoder)

    @BeforeTest
    fun setUp() {
        clearMocks(userRepository)
    }

    @Test
    @Order(1)
    fun `should create a user using CPF`() {
        val cpf = "48024771802"
        val id = UUID.randomUUID()
        val password = "pass123"
        val userRole = UserRole.USER
        val user = UserFixtures.mockUser(id = id, cpf = cpf, password = password, userRole = userRole)

        every {
            userRepository.create(any<User>())
        } returns user

        val response = assertDoesNotThrow {
            service.create(user)
        }

        assertEquals(cpf, response.cpf)
        assertEquals(id, response.id)

        verify(exactly = 1) { userRepository.create(any<User>()) }
    }

    @Test
    @Order(2)
    fun `should get a user using Id`() {
        val cpf = "48024771802"
        val id = UUID.randomUUID()
        val password = "pass123"
        val userRole = UserRole.USER
        val user = UserFixtures.mockUser(id = id, cpf = cpf, password = password, userRole = userRole)

        every { userRepository.findUserById(any()) } returns user

        val response = assertDoesNotThrow {
            service.findUserById(id = id)
        }

        assertNotNull(response)
        assertEquals(id, response.id)
        assertEquals(cpf, response.cpf)

        verify(exactly = 1) { userRepository.findUserById(any()) }
    }

    @Test
    @Order(3)
    fun `should get a user using CPF`() {
        val cpf = "48024771802"
        val id = UUID.randomUUID()
        val password = "pass123"
        val userRole = UserRole.USER
        val user = UserFixtures.mockUser(id = id, cpf = cpf, password = password, userRole = userRole)

        every { userRepository.findUserByCpf(any()) } returns user

        val response = assertDoesNotThrow {
            service.findUserByCpf(cpf = cpf)
        }

        assertNotNull(response)
        assertEquals(id, response.id)
        assertEquals(cpf, response.cpf)

        verify(exactly = 1) { userRepository.findUserByCpf(any()) }
    }
}
