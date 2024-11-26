package com.rooplor.classcraftbackend.controllers

import com.rooplor.classcraftbackend.dtos.InitClassDTO
import com.rooplor.classcraftbackend.dtos.Response
import com.rooplor.classcraftbackend.dtos.VenueStatusDTO
import com.rooplor.classcraftbackend.entities.Classroom
import com.rooplor.classcraftbackend.services.ClassService
import com.rooplor.classcraftbackend.types.DateWithVenue
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
        val modelMapper: ModelMapper,
        private val classService: ClassService,
    ) {
        @Operation(summary = "Get all classes with registration status and published status, or by owners if provided")
        @GetMapping("")
        fun findAllClassPublishedOrByOwners(
            @RequestParam(name = "registrationStatus", required = false) registrationStatus: Boolean?,
            @RequestParam(name = "userId", required = false) userId: List<String>?,
        ): ResponseEntity<Response<List<Classroom>>> =
            try {
                val result =
                    if (userId != null && userId.isNotEmpty()) {
                        classService.findClassByOwners(userId)
                    } else if (registrationStatus != null) {
                        classService.findAllClassPublishedWithRegistrationCondition(registrationStatus)
                    } else {
                        classService.findAllClassPublished()
                    }
                ResponseEntity.ok(Response(success = true, result = result, error = null))
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
                            classService.updateClass(id, modelMapper.map(updatedClass, Classroom::class.java)),
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
                            classService.findClassById(id),
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
                            classService.insertClass(modelMapper.map(addedClass, Classroom::class.java)),
                        error = null,
                    ),
                )
            } catch (e: Exception) {
                ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
            }

        @Operation(summary = "Update venue of a class")
        @PatchMapping("/{id}/venue")
        fun updateDateWithVenueClass(
            @PathVariable id: String,
            @RequestBody venueId: List<DateWithVenue>,
        ): ResponseEntity<Response<Classroom>> =
            try {
                ResponseEntity.ok(
                    Response(
                        success = true,
                        result =
                            classService.updateDateWithVenueClass(id, venueId),
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
                            classService.updateMeetingUrlClass(id, meetingUrl),
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
                            classService.updateContent(id, content),
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
                            classService.updateRegistrationUrl(id, registration),
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
                            classService.toggleRegistrationStatus(id),
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
                            classService.togglePublishStatus(id),
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
                            classService.deleteClass(id),
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
            @RequestParam(name = "status", required = false) stepperStatus: Int,
        ): ResponseEntity<Response<Classroom>> =
            try {
                ResponseEntity.ok(
                    Response(
                        success = true,
                        result =
                            classService.updateStepperStatus(id, stepperStatus),
                        error = null,
                    ),
                )
            } catch (e: Exception) {
                ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
            }

        @Operation(summary = "Reserve a venue for a class")
        @PostMapping("/{id}/reservation")
        fun reserveVenue(
            @PathVariable id: String,
            @RequestBody reservation: List<DateWithVenue>,
        ): ResponseEntity<Response<Boolean>> =
            try {
                classService.reservationVenue(classService.findClassById(id), reservation)
                ResponseEntity.ok(Response(success = true, result = true, error = null))
            } catch (e: Exception) {
                ResponseEntity.badRequest().body(Response(success = false, result = false, error = e.message))
            }

        @Operation(summary = "Update venue status of a class")
        @PatchMapping("/{id}/venue-status")
        fun updateVenueStatus(
            @PathVariable id: String,
            @RequestBody(required = true) venueStatusDTO: VenueStatusDTO,
        ): ResponseEntity<Response<Classroom>> =
            try {
                ResponseEntity.ok(
                    Response(
                        success = true,
                        result =
                            classService.updateVenueStatus(id, venueStatusDTO.venueStatusId, venueStatusDTO.rejectReason ?: ""),
                        error = null,
                    ),
                )
            } catch (e: Exception) {
                ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
            }

        @Operation(summary = "Search classes by title or details")
        @GetMapping("/search")
        fun searchClass(
            @RequestParam(name = "keyword", required = false) keyword: String,
        ): ResponseEntity<Response<List<Classroom>>> =
            try {
                val result = classService.searchClassByTitleOrDetails(keyword)
                ResponseEntity.ok(Response(success = true, result = result, error = null))
            } catch (e: Exception) {
                ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
            }
    }
