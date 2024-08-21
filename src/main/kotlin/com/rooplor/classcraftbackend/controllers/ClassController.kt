package com.rooplor.classcraftbackend.controllers

import com.rooplor.classcraftbackend.dtos.ClassList
import com.rooplor.classcraftbackend.dtos.InitClassDTO
import com.rooplor.classcraftbackend.dtos.VenueUpdateDTO
import com.rooplor.classcraftbackend.entities.Class
import com.rooplor.classcraftbackend.services.ClassService
import com.rooplor.classcraftbackend.utils.ListMapper
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
        val listMapper: ListMapper,
    ) {
        @Operation(summary = "Get all classes")
        @GetMapping("")
        fun findAll(): List<ClassList> = listMapper.mapList(service.findAllClass(), ClassList::class.java, modelMapper)

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

        @Operation(summary = "Update venue of a class")
        @PatchMapping("/{id}/venue")
        fun updateVuenueClass(
            @PathVariable id: String,
            @RequestBody venueUpdateDTO: VenueUpdateDTO,
        ): Class = service.updateVenueClass(id, venueUpdateDTO)

        @Operation(summary = "Update meeting url of a class")
        @PatchMapping("/{id}/meeting-url")
        fun updateMeetingUrlClass(
            @PathVariable id: String,
            @RequestBody meetingUrl: String,
        ): Class = service.updateMeetingUrlClass(id, meetingUrl)

        @Operation(summary = "Update content of a class")
        @PatchMapping("/{id}/content")
        fun updateContent(
            @PathVariable id: String,
            @RequestBody content: String,
        ): Class = service.updateContent(id, content)

        @Operation(summary = "Update registration url of a class")
        @PatchMapping("/{id}/registration-url")
        fun updateRegistrationUrl(
            @PathVariable id: String,
            @RequestBody registration: String,
        ): Class = service.updateRegistrationUrl(id, registration)

        @Operation(summary = "Toggle registration status of a class")
        @PatchMapping("/{id}/toggle-registration-status")
        fun toggleRegistrationStatus(
            @PathVariable id: String,
        ): Class = service.toggleRegistrationStatus(id)

        @Operation(summary = "Toggle publish status of a class")
        @PatchMapping("/{id}/toggle-publish-status")
        fun togglePublicationStatus(
            @PathVariable id: String,
        ): Class = service.togglePublishStatus(id)
    }
