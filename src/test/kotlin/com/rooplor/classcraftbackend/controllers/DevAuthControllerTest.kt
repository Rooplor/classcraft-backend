package com.rooplor.classcraftbackend.controllers

import com.rooplor.classcraftbackend.dtos.Response
import com.rooplor.classcraftbackend.dtos.Token
import com.rooplor.classcraftbackend.services.DevAuthService
import com.rooplor.classcraftbackend.services.cookie.CookieService
import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import kotlin.test.Test

@SpringBootTest
class DevAuthControllerTest {
    @MockBean
    private lateinit var devAuthService: DevAuthService

    @MockBean
    private lateinit var cookieService: CookieService

    @MockBean
    private lateinit var response: HttpServletResponse

    @Test
    fun `should login and set cookies`() {
        val accessToken = "testAccessToken"
        val refreshToken = "testRefreshToken"
        val token = Token(accessToken, refreshToken)

        `when`(devAuthService.login()).thenReturn(token)
        `when`(cookieService.createCookie(anyString(), anyString(), anyInt())).thenReturn(mock())

        val devAuthController = DevAuthController(devAuthService, cookieService)
        val result: ResponseEntity<Response<Boolean>> = devAuthController.login(response)

        verify(response, times(2)).addCookie(any())
        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals(true, result.body?.success)
        assertEquals(true, result.body?.result)
        assertEquals(null, result.body?.error)
    }
}
