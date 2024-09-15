package com.rooplor.classcraftbackend.controllers

import com.rooplor.classcraftbackend.dtos.Response
import com.rooplor.classcraftbackend.dtos.ValidateTokenRequest
import com.rooplor.classcraftbackend.services.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
) {
    @PostMapping("/validate")
    fun validateToken(
        @RequestBody request: ValidateTokenRequest,
    ): ResponseEntity<Response<Boolean>> {
        val token = authService.validateIdToken(request.idToken)
        return if (token != null) {
            ResponseEntity.ok(Response(success = true, result = true, error = null))
        } else {
            ResponseEntity.status(401).body(Response(success = false, result = false, error = "Invalid token"))
        }
    }
}
