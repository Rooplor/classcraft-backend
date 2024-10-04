package com.rooplor.classcraftbackend.dtos

data class Token(
    val accessToken: String,
    val refreshToken: String,
)
