package com.rooplor.classcraftbackend

import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class ClassCraftBackendApplication

fun main(args: Array<String>) {
    runApplication<ClassCraftBackendApplication>(*args)
}
