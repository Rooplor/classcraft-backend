package com.rooplor.classcraftbackend.controllers

import com.rooplor.classcraftbackend.constant.Age
import com.rooplor.classcraftbackend.dtos.Response
import com.rooplor.classcraftbackend.dtos.ValidateTokenRequest
import com.rooplor.classcraftbackend.messages.ErrorMessages
import com.rooplor.classcraftbackend.services.AuthService
import com.rooplor.classcraftbackend.services.cookie.CookieService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
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
    private val acToken = "accessToken"
    private val rfToken = "refreshToken"

    @PostMapping("/login")
    fun login(
        @RequestBody request: ValidateTokenRequest,
        response: HttpServletResponse,
    ): ResponseEntity<Response<Boolean>> {
        val token = authService.login(request.idToken)
        return if (token != null) {
            val accessTokenCookie = cookieService.createCookie(acToken, token.accessToken, Age.COOKIE_AGE)
            val refreshTokenCookie = cookieService.createCookie(rfToken, token.refreshToken, Age.COOKIE_AGE)
            response.addCookie(accessTokenCookie)
            response.addCookie(refreshTokenCookie)
            ResponseEntity.ok(Response(success = true, result = true, error = null))
        } else {
            ResponseEntity.status(401).body(Response(success = false, result = false, error = ErrorMessages.FIREBASE_INVALID_ID_TOKEN))
        }
    }

    @GetMapping("/logout")
    fun logout(response: HttpServletResponse): ResponseEntity<Response<Boolean>> {
        val accessTokenCookie = cookieService.deleteCookie(acToken)
        val refreshTokenCookie = cookieService.deleteCookie(rfToken)
        response.addCookie(accessTokenCookie)
        response.addCookie(refreshTokenCookie)
        return ResponseEntity.ok(Response(success = true, result = true, error = null))
    }
}
