package com.kyle.auth.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.time.Instant
import java.util.Date
import javax.crypto.SecretKey
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class JwtService(
    @Value("\${auth.jwt.secret}") secret: String,
    @Value("\${auth.jwt.issuer}") private val issuer: String,
    @Value("\${auth.jwt.access-ttl-seconds}") private val accessTtl: Long,
    @Value("\${auth.jwt.refresh-ttl-seconds}") private val refreshTtl: Long,
) {
    private val key: SecretKey = Keys.hmacShaKeyFor(secret.toByteArray())

    fun issueAccess(sub: String, app: String, perms: Collection<String>): String =
        buildToken(sub, app, perms, accessTtl, "access")

    fun issueRefresh(sub: String, app: String): String =
        buildToken(sub, app, emptyList(), refreshTtl, "refresh")

    fun parse(token: String) =
        Jwts.parserBuilder().setSigningKey(key).requireIssuer(issuer).build().parseClaimsJws(token).body

    private fun buildToken(sub: String, app: String, perms: Collection<String>, ttl: Long, type: String): String {
        val now = Instant.now()
        return Jwts.builder()
            .setIssuer(issuer)
            .setSubject(sub)
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(now.plusSeconds(ttl)))
            .claim("typ", type)
            .claim("app", app)
            .claim("perms", perms)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }
}