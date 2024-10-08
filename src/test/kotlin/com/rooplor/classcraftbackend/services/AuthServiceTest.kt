package com.rooplor.classcraftbackend.services

import org.mockito.MockedStatic
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
class AuthServiceTest {
    @Test
    fun getAuthenticatedUser() {
        val authentication = Mockito.mock(Authentication::class.java)
        val securityContext = Mockito.mock(SecurityContext::class.java)

        Mockito.`when`(authentication.name).thenReturn("user1")
        Mockito.`when`(securityContext.authentication).thenReturn(authentication)

        val mockedSecurityContextHolder: MockedStatic<SecurityContextHolder> = Mockito.mockStatic(SecurityContextHolder::class.java)
        mockedSecurityContextHolder.`when`<SecurityContext> { SecurityContextHolder.getContext() }.thenReturn(securityContext)

        assertEquals("user1", SecurityContextHolder.getContext().authentication.name)

        mockedSecurityContextHolder.close()
    }
}
