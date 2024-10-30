package com.rooplor.classcraftbackend.controllers

import com.rooplor.classcraftbackend.constant.Age
import com.rooplor.classcraftbackend.dtos.Response
import com.rooplor.classcraftbackend.services.DevAuthService
import com.rooplor.classcraftbackend.services.cookie.CookieService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Profile
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/dev/auth")
@Profile("dev")
class DevAuthController(
    private val devAuthService: DevAuthService,
    private val cookieService: CookieService,
) {
    private val acToken = "accessToken"
    private val rfToken = "refreshToken"

    @GetMapping("/login")
    fun login(response: HttpServletResponse): ResponseEntity<Response<Boolean>> {
        val token = devAuthService.login()
        val accessTokenCookie = cookieService.createCookie(acToken, token.accessToken, Age.COOKIE_AGE)
        val refreshTokenCookie = cookieService.createCookie(rfToken, token.refreshToken, Age.COOKIE_AGE)
        response.addCookie(accessTokenCookie)
        response.addCookie(refreshTokenCookie)
        return ResponseEntity.ok(Response(success = true, result = true, error = null))
    }
}
