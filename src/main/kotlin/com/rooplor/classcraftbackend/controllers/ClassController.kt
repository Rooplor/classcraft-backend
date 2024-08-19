package com.rooplor.classcraftbackend.controllers

import com.rooplor.classcraftbackend.entities.Class
import com.rooplor.classcraftbackend.services.ClassService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/class")
class ClassController
    @Autowired
    constructor(
        val service: ClassService,
    ) {
        @Operation(summary = "Get all classes")
        @GetMapping("")
        fun findAll(): List<Class> = service.findAllClass()

        @Operation(summary = "Get class by id")
        @GetMapping("/{id}")
        fun findById(
            @PathVariable id: String,
        ): Class = service.findClassById(id)

        @Operation(summary = "Insert a new class")
        @PostMapping("")
        fun insertClass(
            @RequestBody addedClass: Class,
        ): Class = service.insertClass(addedClass)
    }
