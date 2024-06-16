package io.github.soat7.myburguercontrol.config

import io.github.soat7.myburguercontrol.business.service.CustomUserDetailsService
import io.github.soat7.myburguercontrol.business.service.TokenService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val userDetailsService: CustomUserDetailsService,
    private val tokenService: TokenService,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val authHeader: String? = request.getHeader(HttpHeaders.AUTHORIZATION)

        //  Verifica se possui cabeçalho de autorização, se não encerra aqui
        if (authHeader.doesNotContainsBearerToken()) {
            filterChain.doFilter(request, response)
            return
        }

        //  Extrair Token
        val jwtToken = authHeader!!.extractTokenValue()
        val email = tokenService.extractEmail(jwtToken)

        // Se email nao estar nulo e não pode tê autenticação presente
        if (email != null && SecurityContextHolder.getContext().authentication == null) {
            val foundUser = userDetailsService.loadUserByUsername(email)

            if (tokenService.isValid(jwtToken, foundUser)) {
                updateContext(foundUser, request)
            }

            filterChain.doFilter(request, response)
        }
    }

    private fun updateContext(foundUser: UserDetails, request: HttpServletRequest) {
        val authToken = UsernamePasswordAuthenticationToken(foundUser, null, foundUser.authorities)
        authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = authToken
    }

    private fun String.extractTokenValue(): String =
//  this.substringAfter("Bearer ")
        this.substringAfter("Bearer ").substringBefore("<")

    private fun String?.doesNotContainsBearerToken(): Boolean =
        this == null || !this.startsWith("Bearer ")
}
