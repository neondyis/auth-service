package com.kyle.auth.security

import com.kyle.auth.repo.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsAdapter(
    private val users: UserRepository,
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val u = users.findByUsername(username) ?: throw UsernameNotFoundException(username)
        return User.withUsername(u.username).password(u.passwordHash).disabled(!u.enabled).authorities(emptyList()).build()
    }
}