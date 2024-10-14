package com.rooplor.classcraftbackend.controllers

import com.rooplor.classcraftbackend.configs.TestConfig
import com.rooplor.classcraftbackend.dtos.Token
import com.rooplor.classcraftbackend.services.AuthService
import com.rooplor.classcraftbackend.services.cookie.CookieService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

@WebMvcTest(AuthController::class)
@Import(TestConfig::class)
class AuthControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var authService: AuthService

    @MockBean
    private lateinit var cookieService: CookieService

    private val cookieAge = 7 * 24 * 60 * 60 // 7 days

    @Test
    fun `test login valid`() {
        val idToken = "validToken"
        val accessToken = "accessToken"
        val refreshToken = "refreshToken"
        val tokenResponse = Token(accessToken, refreshToken)

        `when`(authService.login(idToken)).thenReturn(tokenResponse)
        `when`(cookieService.createCookie("accessToken", accessToken, cookieAge)).thenReturn(null)
        `when`(cookieService.createCookie("refreshToken", refreshToken, cookieAge)).thenReturn(null)

        mockMvc
            .perform(
                post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"idToken\": \"$idToken\"}"),
            )
    }

    @Test
    fun `test login invalid`() {
        val idToken = "invalidToken"
        `when`(authService.login(idToken)).thenReturn(null)

        mockMvc
            .perform(
                post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"idToken\": \"$idToken\"}"),
            )
    }
}
