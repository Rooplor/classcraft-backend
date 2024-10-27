package com.rooplor.classcraftbackend.services

import com.rooplor.classcraftbackend.dtos.Token
import com.rooplor.classcraftbackend.utils.JwtUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest
class DevAuthServiceTest {
    @MockBean
    private lateinit var userService: UserService

    @MockBean
    private lateinit var jwtUtil: JwtUtil

    @Test
    fun `login should create user if not exists and return token`() {
        val username = "dev"
        val accessToken = "testAccessToken"
        val refreshToken = "testRefreshToken"
        val token = Token(accessToken, refreshToken)

        `when`(userService.isUserExistByUsername(username)).thenReturn(false)
        `when`(jwtUtil.generateToken(username)).thenReturn(accessToken)
        `when`(jwtUtil.generateRefreshToken(username)).thenReturn(refreshToken)

        val devAuthService = DevAuthService(userService, jwtUtil)
        val result = devAuthService.login()

        assertEquals(token, result)
    }

    @Test
    fun `login should return token if user exists`() {
        val username = "dev"
        val accessToken = "testAccessToken"
        val refreshToken = "testRefreshToken"
        val token = Token(accessToken, refreshToken)

        `when`(userService.isUserExistByUsername(username)).thenReturn(true)
        `when`(jwtUtil.generateToken(username)).thenReturn(accessToken)
        `when`(jwtUtil.generateRefreshToken(username)).thenReturn(refreshToken)

        val devAuthService = DevAuthService(userService, jwtUtil)
        val result = devAuthService.login()

        assertEquals(token, result)
    }
}
