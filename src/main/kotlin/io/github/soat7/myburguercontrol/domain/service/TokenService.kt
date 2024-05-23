package io.github.soat7.myburguercontrol.domain.service

import io.github.soat7.myburguercontrol.application.ports.inbound.TokenServicePort
import io.github.soat7.myburguercontrol.infrastructure.config.JwtProperties
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import java.util.Date

class TokenService(
    jwtProperties: JwtProperties
) : TokenServicePort {
    private val secretKey = Keys.hmacShaKeyFor(
        jwtProperties.key.toByteArray()
    )

    /*  Função recebera 3 argumentos: user details, data de expiração e declarações adicionais
        Sera para atualiza para acesso e atualização dos tokens
    */

    override fun generate(
        userDetails: UserDetails,
        expirationDate: Date,
        additionalClaims: Map<String, Any>
    ): String =
        Jwts.builder()
            .claims()
            .subject(userDetails.username)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(expirationDate)
            .add(additionalClaims)
            .and()
            .signWith(secretKey)
            .compact()

    /*  Retornar todas Claims (reinvidicações) do nosso token
    */
    private fun getAllClaims(token: String): Claims {
        val parser = Jwts.parser()
            .verifyWith(secretKey)
            .build()

        return parser
            .parseSignedClaims(token)
            .payload
    }

    override fun extractEmail(token: String): String? =
        getAllClaims(token)
            .subject

    //  Verifica se o token expirou olhando a data atual
    override fun isExpired(token: String): Boolean =
        getAllClaims(token)
            .expiration
            .before(Date(System.currentTimeMillis()))

    //  verifica se o token é valido se: o e-mail está correto e não está expirado
    override fun isValid(token: String, userDetails: UserDetails): Boolean {
        val email = extractEmail(token)

        return userDetails.username == email && !isExpired(token)
    }
}
