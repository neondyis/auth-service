package com.kyle.auth.api

data class LoginRequest(
    val username: String,
    val password: String,
    val appKey: String,
)

data class TokenResponse(
    val tokenType: String = "Bearer",
    val accessToken: String,
    val refreshToken: String,
    val app: String,
    val permissions: List<String>,
)