package com.rooplor.classcraftbackend.controller

import com.rooplor.classcraftbackend.entities.Class
import com.rooplor.classcraftbackend.service.ClassService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
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
        @GetMapping("")
        fun findAll(): List<Class> = service.findAllClass()

        @PostMapping("")
        fun insertClass(
            @RequestBody addedClass: Class,
        ): Class = service.insertClass(addedClass)
    }
