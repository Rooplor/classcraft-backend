package com.rooplor.classcraftbackend.filters

import com.rooplor.classcraftbackend.services.cookie.CookieService
import com.rooplor.classcraftbackend.utils.JwtUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtRequestFilter : OncePerRequestFilter() {
    @Autowired
    private lateinit var jwtUtil: JwtUtil

    @Autowired
    private lateinit var userDetailsService: UserDetailsService

    @Autowired
    private lateinit var cookieService: CookieService

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
    ) {
        val jwtCookie = cookieService.getCookie(request, "accessToken")
        val jwt = jwtCookie?.value ?: ""

        var username: String? = null
        if (jwt != "") {
            username = jwtUtil.extractUsername(jwt)
        }

        if (username != null && SecurityContextHolder.getContext().authentication == null) {
            val userDetails = userDetailsService.loadUserByUsername(username)

            if (jwtUtil.validateToken(jwt, userDetails.username)) {
                val authenticationToken = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                authenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authenticationToken
            } else {
                val refreshTokenCookie = cookieService.getCookie(request, "refreshToken")
                val refreshToken = refreshTokenCookie?.value ?: ""

                if (refreshToken.isNotEmpty() && jwtUtil.validateToken(refreshToken, userDetails.username)) {
                    val newAccessToken = jwtUtil.generateToken(userDetails.username)
                    val newRefreshToken = jwtUtil.generateRefreshToken(userDetails.username)
                    val newAccessTokenCookie = cookieService.createCookie("accessToken", newAccessToken, 3600)
                    val newRefreshTokenCookie = cookieService.createCookie("refreshToken", newRefreshToken, 604800)
                    response.addCookie(newAccessTokenCookie)
                    response.addCookie(newRefreshTokenCookie)
                    val authenticationToken = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                    authenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authenticationToken
                }
            }
        }
        chain.doFilter(request, response)
    }
}
