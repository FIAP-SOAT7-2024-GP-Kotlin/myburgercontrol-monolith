package io.github.soat7.myburguercontrol.domain.service

import io.github.soat7.myburguercontrol.application.ports.inbound.CustomUserDetailsServicePort
import io.github.soat7.myburguercontrol.infrastructure.persistence.user.repository.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException

typealias ApplicationUser = io.github.soat7.myburguercontrol.infrastructure.persistence.user.entity.UserEntity

class CustomUserDetailsService(
    private val userRepository: UserRepository
) : CustomUserDetailsServicePort {

    override fun loadUserByUsername(cpf: String): UserDetails =
        userRepository.findByCpf(cpf)
            ?.mapToUserDetails()
            ?: throw UsernameNotFoundException("Cpf not found: $cpf")

    private fun ApplicationUser.mapToUserDetails(): UserDetails =
        User.builder()
            .username(this.cpf)
            .password(this.password)
            .roles(this.role.name)
            .build()
}
