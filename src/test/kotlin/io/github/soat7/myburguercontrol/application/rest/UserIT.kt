package io.github.soat7.myburguercontrol.application.rest

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.soat7.myburguercontrol.base.BaseIntegrationTest
import io.github.soat7.myburguercontrol.domain.model.Role
import io.github.soat7.myburguercontrol.fixtures.AuthFixtures
import io.github.soat7.myburguercontrol.fixtures.UserFixtures
import io.github.soat7.myburguercontrol.infrastructure.persistence.user.repository.UserRepository
import io.github.soat7.myburguercontrol.infrastructure.rest.api.UserResponse
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UserIT : BaseIntegrationTest() {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var enconder: PasswordEncoder

    @Test
    fun `should successfully create a user`() {
        val cpf = "29257035859"
        val password = "pass123"
        val role = Role.USER
        val inputUserData = UserFixtures.mockUserCreationRequest(cpf, password, role)

        val response = restTemplate.postForEntity<UserResponse>("/user", inputUserData)

        assertAll(
            Executable { assertTrue(response.statusCode.is2xxSuccessful) },
            Executable { assertNotNull(response.body?.id ?: String) },
            Executable { assertEquals(cpf, response.body!!.cpf) },
            Executable { assertEquals(role, response.body!!.role) }
        )

        val savedUser = userRepository.findByIdOrNull(response.body!!.id)

        assertAll(
            Executable { assertNotNull(savedUser) },
            Executable { assertEquals(cpf, savedUser!!.cpf) },
            Executable { assertEquals(response.body!!.id, savedUser!!.id) }
        )

    }

    @Test
    fun `should successfully find a user by id`() {
        val cpf = "12345678901"
        val password = "pass123"
        val user = userRepository.save(UserFixtures.mockUserEntity(cpf = cpf, password = enconder.encode(password)))

        val token = getToken(cpf, password) ?: return
        val headers = HttpHeaders()
        headers.setBearerAuth(token)
        val entity = HttpEntity<Any>(headers)

        val response = restTemplate.exchange(
            "/user/{id}",
            HttpMethod.GET,
            entity,
            UserResponse::class.java,
            mapOf("id" to user.id)
        )
        assertAll(
            Executable { assertTrue(response.statusCode.is2xxSuccessful) },
            Executable { assertNotNull(response.body) },
            Executable { assertEquals(user.id, response.body!!.id) },
            Executable { assertEquals(cpf, response.body!!.cpf) }
        )
    }

    @Test
    fun `should return UNAUTHORIZED when password is wrong`() {
        val cpf = "12345678901"
        val password = "pass123"
        val randomId = UUID.randomUUID()
        userRepository.save(UserFixtures.mockUserEntity(cpf = cpf, password = enconder.encode(password)))

        val token = getToken(cpf, "1234") ?: return
        val headers = HttpHeaders()
        headers.setBearerAuth(token)
        val entity = HttpEntity<Any>(headers)

        val response = restTemplate.exchange(
            "/user/{id}",
            HttpMethod.GET,
            entity,
            UserResponse::class.java,
            mapOf("id" to randomId.toString())
        )

        assertEquals(response.statusCode.value(), HttpStatus.UNAUTHORIZED.value())
    }

    @Test
    fun `should return NOT_FOUND when no user is found for the given Id`() {
        val cpf = "12345678901"
        val password = "pass123"
        val randomId = UUID.randomUUID()
        userRepository.save(UserFixtures.mockUserEntity(cpf = cpf, password = enconder.encode(password)))

        val token = getToken(cpf, password) ?: return
        val headers = HttpHeaders()
        headers.setBearerAuth(token)
        val entity = HttpEntity<Any>(headers)

        val response = restTemplate.exchange(
            "/user/{id}",
            HttpMethod.GET,
            entity,
            UserResponse::class.java,
            mapOf("id" to randomId.toString())
        )

        assertEquals(response.statusCode.value(), HttpStatus.NOT_FOUND.value())
    }

    @Test
    fun `should successfully find a user by cpf`() {
        val cpf = "12345678901"
        val password = "pass123"
        userRepository.save(UserFixtures.mockUserEntity(cpf = cpf, password = enconder.encode(password)))

        val user = userRepository.save(UserFixtures.mockUserEntity(cpf = cpf))

        val token = getToken(cpf, password) ?: return
        val headers = HttpHeaders()
        headers.setBearerAuth(token)
        val entity = HttpEntity<Any>(headers)

        val response = restTemplate.exchange(
            "/customer?cpf={cpf}",
            HttpMethod.GET,
            entity,
            UserResponse::class.java,
            mapOf("cpf" to user.cpf)
        )

        assertAll(
            Executable { assertTrue(response.statusCode.is2xxSuccessful) },
            Executable { assertNotNull(response.body) },
            Executable { assertEquals(user.id, response.body!!.id) },
            Executable { assertEquals(cpf, response.body!!.cpf) }
        )
    }

    fun getToken(cpf: String, password: String): String? {
        val inputAuthData = AuthFixtures.mockAuthCreationRequest(cpf, password)

        val response = restTemplate.postForEntity<String>("/auth", inputAuthData)

        val mapper = jacksonObjectMapper()
        val responseBody = response.body ?: throw RuntimeException("Response for /auth is empty")

        val responseMap: Map<String, String> = mapper.readValue(responseBody)

        return responseMap["accessToken"]
    }
}
