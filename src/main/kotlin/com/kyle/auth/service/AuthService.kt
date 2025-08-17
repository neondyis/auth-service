package com.kyle.auth.service

import com.kyle.auth.api.LoginRequest
import com.kyle.auth.api.TokenResponse
import com.kyle.auth.repo.AppClientRepository
import com.kyle.auth.repo.UserAppRoleRepository
import com.kyle.auth.repo.UserRepository
import com.kyle.auth.security.JwtService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * [AuthService]
 */
@Service
open class AuthService(
    private val users: UserRepository,
    private val apps: AppClientRepository,
    private val uar: UserAppRoleRepository,
    private val am: AuthenticationManager,
    private val encoder: PasswordEncoder,
    private val jwt: JwtService,
) {

    @Transactional(readOnly = true)
    open fun login(req: LoginRequest): TokenResponse {
        apps.findBySpecialKey(req.appKey)?.takeIf { it.active } ?: error("app_not_found")
        val user = users.findByUsername(req.username) ?: error("user_not_found")
        am.authenticate(UsernamePasswordAuthenticationToken(req.username, req.password))
        val permissions = uar.findByUsernameAndAppKey(req.username, req.appKey)
            .flatMap { it.role.permissions.map { p -> p.code } }
            .distinct()
            .sorted()
        val access = jwt.issueAccess(user.username, req.appKey, permissions)
        val refresh = jwt.issueRefresh(user.username, req.appKey)
        return TokenResponse(accessToken = access, refreshToken = refresh, app = req.appKey, permissions = permissions)
    }

    @Transactional(readOnly = true)
    open fun refresh(token: String): TokenResponse {
        val claims = jwt.parse(token)
        if (claims["typ"] != "refresh") error("invalid_token_type")
        val username = claims.subject
        val appKey = claims["app"]?.toString() ?: error("missing_app")
        apps.findBySpecialKey(appKey)?.takeIf { it.active } ?: error("app_not_found")
        users.findByUsername(username) ?: error("user_not_found")
        val permissions = uar.findByUsernameAndAppKey(username, appKey)
            .flatMap { it.role.permissions.map { p -> p.code } }
            .distinct()
            .sorted()
        val access = jwt.issueAccess(username, appKey, permissions)
        val refresh = jwt.issueRefresh(username, appKey)
        return TokenResponse(accessToken = access, refreshToken = refresh, app = appKey, permissions = permissions)
    }
}