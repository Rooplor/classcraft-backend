package com.rooplor.classcraftbackend.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseToken
import org.springframework.stereotype.Service

@Service
class AuthService {
    fun validateIdToken(idToken: String): FirebaseToken? =
        try {
            val decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken)
            decodedToken
        } catch (e: FirebaseAuthException) {
            null
        }
}
