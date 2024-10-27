package com.rooplor.classcraftbackend.constant

object Age {
    const val COOKIE_AGE = 7 * 24 * 60 * 60 // 7 days
    const val ACCESS_TOKEN_AGE: Long = 1000 * 60 * 60 * 10 // 10 hours
    const val REFRESH_TOKEN_AGE: Long = 1000 * 60 * 60 * 24 * 7 // 7 days
}
