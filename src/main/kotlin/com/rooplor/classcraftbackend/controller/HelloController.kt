package com.rooplor.classcraftbackend.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController("/api/hello")
class HelloController {
    @GetMapping("/{name}")
    fun hello(
        @PathVariable name: String,
    ): String = "Hello, $name!"
}
