package com.rooplor.classcraftbackend.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class AuthServiceTest {
    private lateinit var authService: AuthService
    private lateinit var firebaseAuth: FirebaseAuth

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        firebaseAuth = Mockito.mock(FirebaseAuth::class.java)
        authService = AuthService()
        Mockito.mockStatic(FirebaseAuth::class.java).use { mockedFirebaseAuth ->
            mockedFirebaseAuth.`when`<FirebaseAuth> { FirebaseAuth.getInstance() }.thenReturn(firebaseAuth)
        }
    }

    @Test
    fun `should return null when token is invalid`() {
        val idToken = "invalidToken"
        Mockito.`when`(firebaseAuth.verifyIdToken(idToken)).thenThrow(FirebaseAuthException::class.java)

        val result = authService.validateIdToken(idToken)

        assertNull(result)
    }
}
