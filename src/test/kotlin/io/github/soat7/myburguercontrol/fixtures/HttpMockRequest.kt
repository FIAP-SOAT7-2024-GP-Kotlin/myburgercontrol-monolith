package io.github.soat7.myburguercontrol.fixtures

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.github.soat7.myburguercontrol.container.MockServerContainer
import org.mockserver.matchers.Times
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import org.mockserver.model.MediaType
import org.springframework.http.HttpStatus

object HttpMockRequest {

    private val objectMapper = getObjectMapper()

    fun mockApprovedPayment() {
        MockServerContainer.client.`when`(
            HttpRequest.request()
                .withPath("/payment-integration")
                .withMethod("POST"),
            Times.once(),
        )
            .respond(
                HttpResponse.response()
                    .withStatusCode(HttpStatus.OK.value())
                    .withBody(
                        objectMapper.writeValueAsString(PaymentResponse.approvedResponseBody()),
                        MediaType.APPLICATION_JSON,
                    ),
            )
    }

    private fun getObjectMapper(): ObjectMapper {
        val objectMapper = ObjectMapper().apply {
            registerModule(JavaTimeModule())
            setSerializationInclusion(JsonInclude.Include.NON_NULL)
            propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE
        }
        return objectMapper
    }
}
