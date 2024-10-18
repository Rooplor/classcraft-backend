package com.rooplor.classcraftbackend.services

import com.rooplor.classcraftbackend.entities.User
import com.rooplor.classcraftbackend.services.logger.LoggerService
import com.rooplor.classcraftbackend.utils.JwtUtil
import org.mockito.MockedStatic
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@SpringBootTest
class AuthServiceTest {
    private val userService = Mockito.mock(UserService::class.java)
    private val loggerService = Mockito.mock(LoggerService::class.java)
    private val userDetailService = Mockito.mock(UserDetailService::class.java)
    private val jwtUtil = Mockito.mock(JwtUtil::class.java)
    private val authService = AuthService(loggerService, userService, userDetailService, jwtUtil)

    @Test
    fun `getAuthenticatedUser should return username when user is authenticated`() {
        val authentication = Mockito.mock(Authentication::class.java)
        val securityContext = Mockito.mock(SecurityContext::class.java)

        Mockito.`when`(authentication.name).thenReturn("user1")
        Mockito.`when`(securityContext.authentication).thenReturn(authentication)

        val mockedSecurityContextHolder: MockedStatic<SecurityContextHolder> = Mockito.mockStatic(SecurityContextHolder::class.java)
        mockedSecurityContextHolder.`when`<SecurityContext> { SecurityContextHolder.getContext() }.thenReturn(securityContext)

        assertEquals("user1", SecurityContextHolder.getContext().authentication.name)

        mockedSecurityContextHolder.close()
    }

    @Test
    fun `getAuthenticatedUserDetails should return user details when user is authenticated`() {
        val authentication = Mockito.mock(Authentication::class.java)
        val securityContext = Mockito.mock(SecurityContext::class.java)
        val username = "user1"
        val user = User(username = username, email = "test@example.com", profilePicture = "profilePicUrl")

        Mockito.`when`(authentication.name).thenReturn(username)
        Mockito.`when`(securityContext.authentication).thenReturn(authentication)
        Mockito.`when`(userService.findByUsername(username)).thenReturn(user)

        val mockedSecurityContextHolder: MockedStatic<SecurityContextHolder> = Mockito.mockStatic(SecurityContextHolder::class.java)
        mockedSecurityContextHolder.`when`<SecurityContext> { SecurityContextHolder.getContext() }.thenReturn(securityContext)

        val result = authService.getAuthenticatedUserDetails()

        assertEquals(user, result)
        mockedSecurityContextHolder.close()
    }

    @Test
    fun `getAuthenticatedUserDetails should return null when user is not authenticated`() {
        val securityContext = Mockito.mock(SecurityContext::class.java)

        Mockito.`when`(securityContext.authentication).thenReturn(null)

        val mockedSecurityContextHolder: MockedStatic<SecurityContextHolder> = Mockito.mockStatic(SecurityContextHolder::class.java)
        mockedSecurityContextHolder.`when`<SecurityContext> { SecurityContextHolder.getContext() }.thenReturn(securityContext)

        val result = authService.getAuthenticatedUserDetails()

        assertNull(result)
        mockedSecurityContextHolder.close()
    }
}
