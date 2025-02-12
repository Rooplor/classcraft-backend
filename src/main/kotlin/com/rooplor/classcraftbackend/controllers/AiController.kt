package com.rooplor.classcraftbackend.controllers

import com.rooplor.classcraftbackend.services.AiService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/ai")
class AiController
    @Autowired
    constructor(
        val aiService: AiService,
    ) {
        @GetMapping("/{topic}")
        fun getAiResponse(
            @PathVariable classId: String,
        ): String = aiService.getAiResponse(classId)
    }
