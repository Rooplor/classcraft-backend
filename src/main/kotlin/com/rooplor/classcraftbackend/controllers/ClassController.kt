package com.rooplor.classcraftbackend.controllers

import com.rooplor.classcraftbackend.dtos.ClassListDTO
import com.rooplor.classcraftbackend.dtos.InitClassDTO
import com.rooplor.classcraftbackend.dtos.Response
import com.rooplor.classcraftbackend.entities.Classroom
import com.rooplor.classcraftbackend.services.ClassService
import com.rooplor.classcraftbackend.utils.ListMapper
import io.swagger.v3.oas.annotations.Operation
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
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
        ): ResponseEntity<Response<List<ClassListDTO>>> =
            try {
                ResponseEntity.ok(
                    Response(
                        success = true,
                        result =
                            listMapper.mapList(
                                service.findAllClassPublished(registrationStatus),
                                ClassListDTO::class.java,
                                modelMapper,
                            ),
                        error = null,
                    ),
                )
            } catch (e: Exception) {
                ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
            }

        @Operation(summary = "Update a class")
        @PutMapping("/{id}")
        fun updateClass(
            @RequestBody updatedClass: InitClassDTO,
            @PathVariable id: String,
        ): ResponseEntity<Response<Classroom>> =
            try {
                ResponseEntity.ok(
                    Response(
                        success = true,
                        result =
                            service.updateClass(id, modelMapper.map(updatedClass, Classroom::class.java)),
                        error = null,
                    ),
                )
            } catch (e: Exception) {
                ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
            }

        @Operation(summary = "Get class by id")
        @GetMapping("/{id}")
        fun findById(
            @PathVariable id: String,
        ): ResponseEntity<Response<Classroom>> =
            try {
                ResponseEntity.ok(
                    Response(
                        success = true,
                        result =
                            service.findClassById(id),
                        error = null,
                    ),
                )
            } catch (e: Exception) {
                ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
            }

        @Operation(summary = "Insert a new class")
        @PostMapping("")
        fun insertClass(
            @RequestBody addedClass: InitClassDTO,
        ): ResponseEntity<Response<Classroom>> =
            try {
                ResponseEntity.ok(
                    Response(
                        success = true,
                        result =
                            service.insertClass(modelMapper.map(addedClass, Classroom::class.java)),
                        error = null,
                    ),
                )
            } catch (e: Exception) {
                ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
            }

        @Operation(summary = "Update venue of a class")
        @PatchMapping("/{id}/venue/{venueId}")
        fun updateVenueClass(
            @PathVariable id: String,
            @PathVariable venueId: String,
        ): ResponseEntity<Response<Classroom>> =
            try {
                ResponseEntity.ok(
                    Response(
                        success = true,
                        result =
                            service.updateVenueClass(id, venueId),
                        error = null,
                    ),
                )
            } catch (e: Exception) {
                ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
            }

        @Operation(summary = "Update meeting url of a class")
        @PatchMapping("/{id}/meeting-url")
        fun updateMeetingUrlClass(
            @PathVariable id: String,
            @RequestBody meetingUrl: String,
        ): ResponseEntity<Response<Classroom>> =
            try {
                ResponseEntity.ok(
                    Response(
                        success = true,
                        result =
                            service.updateMeetingUrlClass(id, meetingUrl),
                        error = null,
                    ),
                )
            } catch (e: Exception) {
                ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
            }

        @Operation(summary = "Update content of a class")
        @PatchMapping("/{id}/content")
        fun updateContent(
            @PathVariable id: String,
            @RequestBody content: String,
        ): ResponseEntity<Response<Classroom>> =
            try {
                ResponseEntity.ok(
                    Response(
                        success = true,
                        result =
                            service.updateContent(id, content),
                        error = null,
                    ),
                )
            } catch (e: Exception) {
                ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
            }

        @Operation(summary = "Update registration url of a class")
        @PatchMapping("/{id}/registration-url")
        fun updateRegistrationUrl(
            @PathVariable id: String,
            @RequestBody registration: String,
        ): ResponseEntity<Response<Classroom>> =
            try {
                ResponseEntity.ok(
                    Response(
                        success = true,
                        result =
                            service.updateRegistrationUrl(id, registration),
                        error = null,
                    ),
                )
            } catch (e: Exception) {
                ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
            }

        @Operation(summary = "Toggle registration status of a class")
        @PatchMapping("/{id}/toggle-registration-status")
        fun toggleRegistrationStatus(
            @PathVariable id: String,
        ): ResponseEntity<Response<Classroom>> =
            try {
                ResponseEntity.ok(
                    Response(
                        success = true,
                        result =
                            service.toggleRegistrationStatus(id),
                        error = null,
                    ),
                )
            } catch (e: Exception) {
                ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
            }

        @Operation(summary = "Toggle publish status of a class")
        @PatchMapping("/{id}/toggle-publish-status")
        fun togglePublicationStatus(
            @PathVariable id: String,
        ): ResponseEntity<Response<Classroom>> =
            try {
                ResponseEntity.ok(
                    Response(
                        success = true,
                        result =
                            service.togglePublishStatus(id),
                        error = null,
                    ),
                )
            } catch (e: Exception) {
                ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
            }

        @Operation(summary = "Remove a class")
        @DeleteMapping("/{id}")
        fun removeClass(
            @PathVariable id: String,
        ): ResponseEntity<Response<Unit>> =
            try {
                ResponseEntity.ok(
                    Response(
                        success = true,
                        result =
                            service.deleteClass(id),
                        error = null,
                    ),
                )
            } catch (e: Exception) {
                ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
            }

        @Operation(summary = "Find classes by owners")
        @GetMapping("/owners")
        fun findClassByOwners(
            @RequestParam owners: List<String>,
        ): ResponseEntity<Response<List<ClassListDTO>>> =
            try {
                ResponseEntity.ok(
                    Response(
                        success = true,
                        result =
                            listMapper.mapList(service.findClassByOwners(owners), ClassListDTO::class.java, modelMapper),
                        error = null,
                    ),
                )
            } catch (e: Exception) {
                ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
            }

        @Operation(summary = "Update classroom stepper status")
        @PatchMapping("/{id}/stepper-status")
        fun updateClassroomStepperStatus(
            @PathVariable id: String,
        ): ResponseEntity<Response<Classroom>> =
            try {
                ResponseEntity.ok(
                    Response(
                        success = true,
                        result =
                            service.updateStepperStatus(id),
                        error = null,
                    ),
                )
            } catch (e: Exception) {
                ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
            }
    }
