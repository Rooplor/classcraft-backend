package com.rooplor.classcraftbackend.controllers

import com.rooplor.classcraftbackend.dtos.ClassListDTO
import com.rooplor.classcraftbackend.dtos.InitClassDTO
import com.rooplor.classcraftbackend.entities.Classroom
import com.rooplor.classcraftbackend.services.ClassService
import com.rooplor.classcraftbackend.utils.ListMapper
import io.swagger.v3.oas.annotations.Operation
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
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
        @Operation(summary = "Get all classes with registration status and published status")
        @GetMapping("")
        fun findAllClassPublished(
            @RequestParam(name = "registrationStatus") registrationStatus: Boolean,
        ): List<ClassListDTO> = listMapper.mapList(service.findAllClassPublished(registrationStatus), ClassListDTO::class.java, modelMapper)

        @Operation(summary = "Update a class")
        @PutMapping("/{id}")
        fun updateClass(
            @RequestBody updatedClass: InitClassDTO,
            @PathVariable id: String,
        ): Classroom = service.updateClass(id, modelMapper.map(updatedClass, Classroom::class.java))

        @Operation(summary = "Get class by id")
        @GetMapping("/{id}")
        fun findById(
            @PathVariable id: String,
        ): Classroom = service.findClassById(id)

        @Operation(summary = "Insert a new class")
        @PostMapping("")
        fun insertClass(
            @RequestBody addedClass: InitClassDTO,
        ): Classroom = service.insertClass(modelMapper.map(addedClass, Classroom::class.java))

        @Operation(summary = "Update venue of a class")
        @PatchMapping("/{id}/venue/{venueId}")
        fun updateVenueClass(
            @PathVariable id: String,
            @PathVariable venueId: String,
        ): Classroom = service.updateVenueClass(id, venueId)

        @Operation(summary = "Update meeting url of a class")
        @PatchMapping("/{id}/meeting-url")
        fun updateMeetingUrlClass(
            @PathVariable id: String,
            @RequestBody meetingUrl: String,
        ): Classroom = service.updateMeetingUrlClass(id, meetingUrl)

        @Operation(summary = "Update content of a class")
        @PatchMapping("/{id}/content")
        fun updateContent(
            @PathVariable id: String,
            @RequestBody content: String,
        ): Classroom = service.updateContent(id, content)

        @Operation(summary = "Update registration url of a class")
        @PatchMapping("/{id}/registration-url")
        fun updateRegistrationUrl(
            @PathVariable id: String,
            @RequestBody registration: String,
        ): Classroom = service.updateRegistrationUrl(id, registration)

        @Operation(summary = "Toggle registration status of a class")
        @PatchMapping("/{id}/toggle-registration-status")
        fun toggleRegistrationStatus(
            @PathVariable id: String,
        ): Classroom = service.toggleRegistrationStatus(id)

        @Operation(summary = "Toggle publish status of a class")
        @PatchMapping("/{id}/toggle-publish-status")
        fun togglePublicationStatus(
            @PathVariable id: String,
        ): Classroom = service.togglePublishStatus(id)

        @Operation(summary = "Remove a class")
        @DeleteMapping("/{id}")
        fun removeClass(
            @PathVariable id: String,
        ): Unit = service.deleteClass(id)

        @Operation(summary = "Find classes by owners")
        @GetMapping("/owners")
        fun findClassByOwners(
            @RequestParam owners: List<String>,
        ): List<ClassListDTO> = listMapper.mapList(service.findClassByOwners(owners), ClassListDTO::class.java, modelMapper)

        @Operation(summary = "Update classroom stepper status")
        @PatchMapping("/{id}/stepper-status")
        fun updateClassroomStepperStatus(
            @PathVariable id: String,
        ): Classroom = service.updateStepperStatus(id)
    }
