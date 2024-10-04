package com.rooplor.classcraftbackend.utils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import java.util.Date
import kotlin.collections.HashMap

@Component
class JwtUtil(
    private val environment: Environment,
) {
    private val secretKey = environment.getProperty("jwt.secret-key") ?: "secretalsdjflajjiernjbuasdfoaljnwiuebrialksjdnfkahsdfjalsdjf"

    fun generateToken(username: String): String {
        val claims: Map<String, Any> = HashMap()
        return createToken(claims, username, 1000 * 60 * 60 * 10) // 10 hours
    }

    fun generateRefreshToken(username: String): String {
        val claims: Map<String, Any> = HashMap()
        return createToken(claims, username, 1000 * 60 * 60 * 24 * 7) // 7 days
    }

    private fun createToken(
        claims: Map<String, Any>,
        subject: String,
        expiredIn: Long,
    ): String =
        Jwts
            .builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + expiredIn))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()

    fun validateToken(
        token: String,
        username: String,
    ): Boolean {
        val extractedUsername = extractUsername(token)
        return (extractedUsername == username && !isTokenExpired(token))
    }

    fun extractUsername(token: String): String = extractClaim(token, Claims::getSubject)

    fun <T> extractClaim(
        token: String,
        claimsResolver: (Claims) -> T,
    ): T {
        val claims = extractAllClaims(token)
        return claimsResolver(claims)
    }

    private fun extractAllClaims(token: String): Claims =
        Jwts
            .parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .body

    private fun isTokenExpired(token: String): Boolean {
        val expiration = extractClaim(token, Claims::getExpiration)
        return expiration.before(Date())
    }
}
