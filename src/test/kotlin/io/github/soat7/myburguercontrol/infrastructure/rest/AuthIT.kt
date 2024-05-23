package io.github.soat7.myburguercontrol.infrastructure.rest

import io.github.soat7.myburguercontrol.base.BaseIntegrationTest
import io.github.soat7.myburguercontrol.fixtures.AuthFixtures
import io.github.soat7.myburguercontrol.fixtures.UserFixtures
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AuthIT : BaseIntegrationTest() {

    @Test
    fun `should successfully authenticate`() {
        val cpf = "29257035859"
        val password = "pass123"
        userRepository.save(UserFixtures.mockUserEntity(cpf = cpf, password = passwordEncoder.encode(password)))

        val inputAuthData = AuthFixtures.mockAuthCreationRequest(cpf, password)

        val response = restTemplate.postForEntity<String>("/auth", inputAuthData)

        assertAll(
            Executable { assertTrue(response.statusCode.is2xxSuccessful) },
            Executable { assertNotNull(response.body) },
            Executable { assertTrue(response.body!!.contains("accessToken")) }
        )
    }

    @Test
    fun `should return BAD CREDENTIALS when password is wrong`() {
        val cpf = "29257035859"
        val password = "pass123"
        userRepository.save(UserFixtures.mockUserEntity(cpf = cpf, password = passwordEncoder.encode(password)))

        val inputAuthData = AuthFixtures.mockAuthCreationRequest(cpf, "wrongPass")

        val response = restTemplate.postForEntity<String>("/auth", inputAuthData)

        assertAll(
            Executable { assertTrue(response.statusCode.is5xxServerError) },
            Executable { assertNotNull(response.body) },
            Executable { assertTrue(response.body!!.contains("Bad Credentials")) }
        )
    }
}
