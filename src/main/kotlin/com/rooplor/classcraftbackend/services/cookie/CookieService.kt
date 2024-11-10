package com.rooplor.classcraftbackend.services.cookie

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service

@Service
class CookieService(
    private val environment: Environment,
) {
    val isCookieSecure: Boolean = environment.getProperty("cookie.secure")?.toBoolean() ?: true

    fun createCookie(
        name: String,
        value: String,
        maxAge: Int,
    ): Cookie {
        // debug
        println("isCookieSecure: $isCookieSecure")
        println("path: ${if (isCookieSecure) "/kp2" else "/"}")
        // debug
        val cookie = Cookie(name, value)
        cookie.isHttpOnly = true
        cookie.path = if (isCookieSecure) "/kp2" else "/"
        cookie.maxAge = maxAge
        cookie.secure = isCookieSecure
        return cookie
    }

    fun getCookie(
        request: HttpServletRequest,
        name: String,
    ): Cookie? {
        val cookies = request.cookies
        if (cookies != null) {
            for (cookie in cookies) {
                if (cookie.name == name) {
                    return cookie
                }
            }
        }
        return null
    }

    fun deleteCookie(name: String): Cookie {
        val cookie = Cookie(name, "")
        cookie.isHttpOnly = true
        cookie.path = if (isCookieSecure) "/kp2" else "/"
        cookie.maxAge = 0
        cookie.secure = isCookieSecure
        return cookie
    }
}
