package com.rooplor.classcraftbackend.configuration

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.media.Schema
import org.springframework.context.annotation.Bean

class SwaggerConfig {
    @Bean
    fun customOpenAPI(): OpenAPI =
        OpenAPI().components(
            Components().addSchemas(
                "MultipartFile",
                Schema<Any>().type("string").format("binary"),
            ),
        )
}
