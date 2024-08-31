package com.rooplor.classcraftbackend.utils

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper

object JsonValid {
    fun isValidJson(content: String): Boolean =
        try {
            ObjectMapper().readTree(content)
            true
        } catch (e: JsonProcessingException) {
            false
        }
}
