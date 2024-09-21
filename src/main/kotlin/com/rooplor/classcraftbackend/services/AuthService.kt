package com.rooplor.classcraftbackend.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseToken
import com.rooplor.classcraftbackend.messages.ErrorMessages
import com.rooplor.classcraftbackend.services.logger.LoggerService
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val loggerService: LoggerService,
) {
    fun validateIdToken(idToken: String): FirebaseToken? =
        try {
            val decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken)
            decodedToken
        } catch (e: Exception) {
            loggerService.error(ErrorMessages.FIREBASE_EXCEPTION + ": $e")
            null
        }
}
