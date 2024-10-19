package com.rooplor.classcraftbackend.configuration

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.annotations.servers.Server

@OpenAPIDefinition(
    info =
        Info(
            contact =
                Contact(
                    name = "Rooplor",
                    email = "rooplorgit@gmail.com",
                ),
            description = "The ClassCraft API definition",
            title = "The ClassCraft API",
            version = "1.0",
        ),
    servers =
        arrayOf(
            Server(
                description = "Local ENV",
                url = "http://localhost:8080",
            ),
            Server(
                description = "QA ENV",
                url = "http://cp24kp2.sit.kmutt.ac.th:5000",
            ),
        ),
)
@SecurityScheme(
    name = "bearerAuth",
    description = "JWT auth description",
    scheme = "bearer",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    `in` = SecuritySchemeIn.HEADER,
)
class OpenApiConfig
