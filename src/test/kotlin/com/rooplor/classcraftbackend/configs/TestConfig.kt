package com.rooplor.classcraftbackend.configs

import com.rooplor.classcraftbackend.services.cookie.CookieService
import com.rooplor.classcraftbackend.utils.JwtUtil
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.core.env.Environment
import org.springframework.mock.env.MockEnvironment

@TestConfiguration
class TestConfig {
    @Bean
    fun jwtUtil(): JwtUtil {
        val environment: Environment =
            MockEnvironment().withProperty(
                "jwt.secret-key",
                "secretsdsdfwefdvfaweerfradfdcawedefadfasdfewsrfasdf",
            )
        return JwtUtil(environment)
    }

    @Bean
    fun cookieService(): CookieService = CookieService()
}
