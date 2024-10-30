package com.rooplor.classcraftbackend.services

import com.rooplor.classcraftbackend.dtos.Token
import com.rooplor.classcraftbackend.entities.User
import com.rooplor.classcraftbackend.utils.JwtUtil
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile("dev")
class DevAuthService(
    private val userService: UserService,
    private val jwtUtil: JwtUtil,
) {
    fun login(): Token {
        val username = "dev"
        val existingUser = userService.isUserExistByUsername(username)
        if (!existingUser) {
            val newUser =
                User(
                    username = username,
                    email = "test@mail.com",
                    profilePicture = "https://media.stickerswiki.app/oneesanstickers117/6747778.512.webp",
                )
            userService.createUser(newUser)
        }
        val accessToken = jwtUtil.generateToken(username)
        val refreshToken = jwtUtil.generateRefreshToken(username)
        return Token(accessToken, refreshToken)
    }
}
