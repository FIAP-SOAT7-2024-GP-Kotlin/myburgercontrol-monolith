package io.github.soat7.myburguercontrol.service

import io.github.soat7.myburguercontrol.config.JwtProperties
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.Date

@Service
class TokenService(
    jwtProperties: JwtProperties
) {
    private val secretKey = Keys.hmacShaKeyFor(
        jwtProperties.key.toByteArray()
    )

    /*  Função recebera 3 argumentos: user details, data de expiração e declarações adicionais
        Sera para atualiza para acesso e atualização dos tokens
     */

    fun generate(
        userDetails: UserDetails,
        expirationDate: Date,
        additionalClaims: Map<String, Any> = emptyMap()
    ): String =
        Jwts.builder() // invoca builder
            .claims()
            .subject(userDetails.username) // para saber quem é o dono
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

    fun extractEmail(token: String): String? =
        getAllClaims(token)
            .subject

    //  Verifica se o token expirou olhando a data atual
    fun isExpired(token: String): Boolean =
        getAllClaims(token)
            .expiration
            .before(Date(System.currentTimeMillis()))

    //  verifica se o token é valido se: o e-mail está correto e não está expirado
    fun isValid(token: String, userDetails: UserDetails): Boolean {
        val email = extractEmail(token)

        return userDetails.username == email && !isExpired(token)
    }
}
