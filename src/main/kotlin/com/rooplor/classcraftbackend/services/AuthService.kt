package com.rooplor.classcraftbackend.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseToken
import com.rooplor.classcraftbackend.dtos.Token
import com.rooplor.classcraftbackend.entities.User
import com.rooplor.classcraftbackend.messages.ErrorMessages
import com.rooplor.classcraftbackend.services.logger.LoggerService
import com.rooplor.classcraftbackend.utils.JwtUtil
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val loggerService: LoggerService,
    private val userService: UserService,
    private val userDetailService: UserDetailService,
    private val jwtUtil: JwtUtil,
) {
    fun login(idToken: String): Token? {
        val decodedToken = validateIdToken(idToken)
        if (decodedToken != null) {
            val existingUser = userService.isUserExistByUsername(decodedToken.name)
            if (!existingUser) {
                val newUser =
                    User(
                        username = decodedToken.name,
                        email = decodedToken.email,
                        profilePicture = decodedToken.picture,
                    )
                userService.createUser(newUser)
            }

            val userDetails = userDetailService.loadUserByUsername(decodedToken.name)
            val accessToken = jwtUtil.generateToken(userDetails.username)
            val refreshToken = jwtUtil.generateRefreshToken(userDetails.username)

            return Token(accessToken, refreshToken)
        }
        return null
    }

    fun getAuthenticatedUser(): String? {
        val authentication = SecurityContextHolder.getContext().authentication
        return authentication?.name
    }

    fun getAuthenticatedUserDetails(): User {
        val username = getAuthenticatedUser() ?: throw Exception(ErrorMessages.USER_NOT_FOUND)
        return userService.findByUsername(username)
    }

    fun getUserId(): String {
        val username = getAuthenticatedUser() ?: throw Exception(ErrorMessages.USER_NOT_FOUND)
        return userService.findByUsername(username).id ?: throw Exception(ErrorMessages.USER_NOT_FOUND)
    }

    private fun validateIdToken(idToken: String): FirebaseToken? =
        try {
            val decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken)
            decodedToken
        } catch (e: Exception) {
            loggerService.error(ErrorMessages.FIREBASE_EXCEPTION + ": $e")
            null
        }
}
