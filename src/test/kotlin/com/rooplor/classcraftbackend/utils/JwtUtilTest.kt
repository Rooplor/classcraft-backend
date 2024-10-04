package com.rooplor.classcraftbackend.utils

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.core.env.Environment
import java.lang.reflect.InvocationTargetException
import java.util.Date

class JwtUtilTest {
    private lateinit var jwtUtil: JwtUtil
    private lateinit var environment: Environment

    @BeforeEach
    fun setUp() {
        environment = Mockito.mock(Environment::class.java)
        Mockito
            .`when`(environment.getProperty("jwt.secret-key"))
            .thenReturn("asdfasdfasdfasdfasdfwerfsfdffsdfzvsdfwrfwerwsewdefrasdfasdfasdfasdfasdfadfasdfsdf")
        jwtUtil = JwtUtil(environment)
    }

    @Test
    fun `isTokenExpired should return true for an expired token`() {
        val claims: Map<String, Any> = HashMap()
        val expiredToken =
            Jwts
                .builder()
                .setClaims(claims)
                .setSubject("testUser")
                .setIssuedAt(Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24))
                .setExpiration(Date(System.currentTimeMillis() - 1000 * 60 * 60))
                .signWith(SignatureAlgorithm.HS256, "asdfasdfasdfasdfasdfwerfsfdffsdfzvsdfwrfwerwsewdefrasdfasdfasdfasdfasdfadfasdfsdf")
                .compact()

        val method = JwtUtil::class.java.getDeclaredMethod("isTokenExpired", String::class.java)
        method.isAccessible = true

        val exception =
            assertThrows(InvocationTargetException::class.java) {
                method.invoke(jwtUtil, expiredToken)
            }

        assertTrue(exception.cause is ExpiredJwtException)
    }
}
