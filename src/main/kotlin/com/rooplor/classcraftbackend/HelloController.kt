package com.rooplor.classcraftbackend

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController("/api/hello")
class HelloController {
    @GetMapping("/{name}")
    fun hello(@PathVariable name: String): String {
        return "Hello $name!"
    }
}