package com.rooplor.classcraftbackend.services.cookie

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`

class CookieServiceTest {
    private val cookieService = CookieService()

    @Test
    fun `createCookie should return a cookie with correct properties`() {
        val name = "testCookie"
        val value = "testValue"
        val maxAge = 3600

        val cookie = cookieService.createCookie(name, value, maxAge)

        assertEquals(name, cookie.name)
        assertEquals(value, cookie.value)
        assertEquals(maxAge, cookie.maxAge)
        assertEquals("/", cookie.path)
        assertEquals(true, cookie.isHttpOnly)
    }

    @Test
    fun `getCookie should return the correct cookie when it exists`() {
        val name = "testCookie"
        val value = "testValue"
        val cookie = Cookie(name, value)
        val request = Mockito.mock(HttpServletRequest::class.java)
        `when`(request.cookies).thenReturn(arrayOf(cookie))

        val result = cookieService.getCookie(request, name)

        assertEquals(cookie, result)
    }

    @Test
    fun `getCookie should return null when the cookie does not exist`() {
        val name = "testCookie"
        val request = Mockito.mock(HttpServletRequest::class.java)
        `when`(request.cookies).thenReturn(arrayOf())

        val result = cookieService.getCookie(request, name)

        assertNull(result)
    }

    @Test
    fun `deleteCookie should return a cookie with maxAge set to 0`() {
        val name = "testCookie"

        val cookie = cookieService.deleteCookie(name)

        assertEquals(name, cookie.name)
        assertEquals("", cookie.value)
        assertEquals(0, cookie.maxAge)
        assertEquals("/", cookie.path)
        assertEquals(true, cookie.isHttpOnly)
    }
}
