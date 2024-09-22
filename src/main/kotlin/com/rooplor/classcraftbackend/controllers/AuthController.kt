package com.rooplor.classcraftbackend.controllers

import com.rooplor.classcraftbackend.dtos.Response
import com.rooplor.classcraftbackend.dtos.ValidateTokenRequest
import com.rooplor.classcraftbackend.services.AuthService
import com.rooplor.classcraftbackend.services.cookie.CookieService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
    private val cookieService: CookieService,
) {
    private val accessTokenCookieAge = 10 * 60 * 60 // 10 hours
    private val refreshTokenCookieAge = 7 * 24 * 60 * 60 // 7 days

    @PostMapping("/validate")
    fun validateToken(
        @RequestBody request: ValidateTokenRequest,
        response: HttpServletResponse,
    ): ResponseEntity<Response<Boolean>> {
        val token = authService.login(request.idToken)
        return if (token != null) {
            val accessTokenCookie = cookieService.createCookie("accessToken", token.accessToken, accessTokenCookieAge)
            val refreshTokenCookie = cookieService.createCookie("refreshToken", token.refreshToken, refreshTokenCookieAge)
            response.addCookie(accessTokenCookie)
            response.addCookie(refreshTokenCookie)
            ResponseEntity.ok(Response(success = true, result = true, error = null))
        } else {
            ResponseEntity.status(401).body(Response(success = false, result = false, error = "Invalid token"))
        }
    }
}
