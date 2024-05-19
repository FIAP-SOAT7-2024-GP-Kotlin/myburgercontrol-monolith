package io.github.soat7.myburguercontrol.application.ports.inbound

import org.springframework.security.core.userdetails.UserDetails
import java.util.Date

interface TokenServicePort {
    fun generate(userDetails: UserDetails, expirationDate: Date, additionalClaims: Map<String, Any> = emptyMap()): String
    fun extractEmail(token: String): String?
    fun isExpired(token: String): Boolean
    fun isValid(token: String, userDetails: UserDetails): Boolean
}
