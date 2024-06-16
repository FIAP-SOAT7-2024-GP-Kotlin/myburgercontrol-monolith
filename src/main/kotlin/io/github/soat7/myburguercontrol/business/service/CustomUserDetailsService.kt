package io.github.soat7.myburguercontrol.business.service

import io.github.soat7.myburguercontrol.database.user.entity.UserEntity
import io.github.soat7.myburguercontrol.database.user.repository.UserJpaRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

typealias ApplicationUser = UserEntity

@Service
class CustomUserDetailsService(
    private val userJpaRepository: UserJpaRepository,
) : UserDetailsService {

    override fun loadUserByUsername(cpf: String): UserDetails =
        userJpaRepository.findByCpf(cpf)
            ?.mapToUserDetails()
            ?: throw UsernameNotFoundException("Cpf not found: $cpf")

    private fun ApplicationUser.mapToUserDetails(): UserDetails =
        User.builder()
            .username(this.cpf)
            .password(this.password)
            .roles(this.role.name)
            .build()
}
