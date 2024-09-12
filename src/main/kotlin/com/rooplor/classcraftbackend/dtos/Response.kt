package com.rooplor.classcraftbackend.dtos

data class Response<T>(
    val success: Boolean,
    val result: T?,
    val error: String?,
)
