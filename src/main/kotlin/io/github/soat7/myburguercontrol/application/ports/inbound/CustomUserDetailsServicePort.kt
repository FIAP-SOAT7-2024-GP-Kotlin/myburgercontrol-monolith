package io.github.soat7.myburguercontrol.application.ports.inbound

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService

interface CustomUserDetailsServicePort : UserDetailsService {
    override fun loadUserByUsername(cpf: String): UserDetails
}
