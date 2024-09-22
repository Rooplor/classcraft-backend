package com.rooplor.classcraftbackend.services.cookie

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Service

@Service
class CookieService {
    fun createCookie(
        name: String,
        value: String,
        maxAge: Int,
    ): Cookie {
        val cookie = Cookie(name, value)
        cookie.isHttpOnly = true
        cookie.path = "/"
        cookie.maxAge = maxAge
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
        cookie.path = "/"
        cookie.maxAge = 0
        return cookie
    }
}
