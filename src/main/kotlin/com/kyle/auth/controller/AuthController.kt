package com.kyle.auth.controller

import com.kyle.auth.api.LoginRequest
import com.kyle.auth.api.TokenResponse
import com.kyle.auth.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.MediaType
import org.springframework.http.HttpHeaders
import jakarta.validation.Valid

@RestController
@RequestMapping("/auth", produces = [MediaType.APPLICATION_JSON_VALUE])
class AuthController(
    private val auth: AuthService,
) {

    @PostMapping("/login", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun login(@Valid @RequestBody req: LoginRequest): ResponseEntity<TokenResponse> =
        ResponseEntity.ok(auth.login(req))

    @PostMapping("/refresh")
    fun refresh(@RequestHeader(HttpHeaders.AUTHORIZATION) bearer: String): ResponseEntity<TokenResponse> {
        val token = bearer.removePrefix("Bearer ").trim()
        return ResponseEntity.ok(auth.refresh(token))
    }

    @GetMapping("/me/perms")
    @PreAuthorize("isAuthenticated()")
    fun myPerms(@RequestHeader(HttpHeaders.AUTHORIZATION) bearer: String): Map<String, Any> =
        mapOf("authorities" to listOf(bearer.length).filter { it >= 0 })
}