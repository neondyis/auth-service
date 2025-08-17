package com.kyle.auth.config

import com.kyle.auth.security.JwtAuthFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.config.Customizer
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableMethodSecurity
open class SecurityConfig(
    private val jwtAuthFilter: JwtAuthFilter,
    private val uds: UserDetailsService,
) {

    @Bean
    open fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    open fun authenticationManager(encoder: PasswordEncoder): AuthenticationManager =
        ProviderManager(
            DaoAuthenticationProvider(encoder)
        )

    @Bean
    open fun filterChain(http: HttpSecurity): SecurityFilterChain =
        http
            .cors(Customizer.withDefaults())
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it.requestMatchers("/actuator/health").permitAll()
                    .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/refresh").permitAll()
                    .requestMatchers(HttpMethod.GET, "/actuator*").permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
}