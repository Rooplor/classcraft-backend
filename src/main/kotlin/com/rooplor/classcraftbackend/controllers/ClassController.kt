package com.rooplor.classcraftbackend.controllers

import com.rooplor.classcraftbackend.dtos.InitClassDTO
import com.rooplor.classcraftbackend.dtos.VenueUpdateDTO
import com.rooplor.classcraftbackend.entities.Class
import com.rooplor.classcraftbackend.services.ClassService
import io.swagger.v3.oas.annotations.Operation
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
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
        val modelMapper: ModelMapper,
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
            @RequestBody addedClass: InitClassDTO,
        ): Class = service.insertClass(modelMapper.map(addedClass, Class::class.java))

        @PatchMapping("/{id}")
        fun updateClass(
            @PathVariable id: String,
            @RequestBody venueUpdateDTO: VenueUpdateDTO,
        ): Class {
            val classToUpdate = service.findClassById(id)
            modelMapper.map(venueUpdateDTO, classToUpdate)
            println(modelMapper.map(venueUpdateDTO, classToUpdate))
            return service.insertClass(classToUpdate)
        }
    }
