package com.rooplor.classcraftbackend.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/hello")
class HelloController {
    @GetMapping("/{name}")
    fun hello(
        @PathVariable name: String,
    ): String = "Hello 123, $name!"
}
