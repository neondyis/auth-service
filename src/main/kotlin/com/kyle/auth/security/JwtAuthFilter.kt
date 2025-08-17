package com.kyle.auth.security

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.http.HttpHeaders

@Component
class JwtAuthFilter(
    private val jwt: JwtService,
    private val mapper: ObjectMapper,
) : OncePerRequestFilter() {

    override fun doFilterInternal(req: HttpServletRequest, res: HttpServletResponse, chain: FilterChain) {
        val auth = req.getHeader(HttpHeaders.AUTHORIZATION)?.takeIf { it.startsWith("Bearer ") }?.removePrefix("Bearer ")
        if (auth.isNullOrBlank()) {
            chain.doFilter(req, res)
            return
        }
        try {
            val claims = jwt.parse(auth)
            val username = claims.subject
            val app = claims["app"]?.toString().orEmpty()
            val perms = (claims["perms"] as? Collection<*>)?.mapNotNull { it?.toString() } ?: emptyList()
            val authorities = perms.map { SimpleGrantedAuthority("$app:$it") }
            SecurityContextHolder.getContext().authentication =
                UsernamePasswordAuthenticationToken(username, null, authorities)
            chain.doFilter(req, res)
        } catch (e: Exception) {
            res.status = 401
            res.contentType = "application/json"
            mapper.writeValue(res.outputStream, mapOf("error" to "invalid_token"))
        }
    }
}