package com.rooplor.classcraftbackend.controllers

import com.rooplor.classcraftbackend.dtos.ReservationDTO
import com.rooplor.classcraftbackend.dtos.Response
import com.rooplor.classcraftbackend.entities.Venue
import com.rooplor.classcraftbackend.services.ClassService
import com.rooplor.classcraftbackend.services.VenueService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/venue")
class VenueController
    @Autowired
    constructor(
        val venueService: VenueService,
        val classService: ClassService,
    ) {
        @Operation(summary = "Get all venues")
        @GetMapping("")
        fun findAll(): ResponseEntity<Response<List<Venue>>> =
            try {
                val venues = venueService.findAllVenue()
                ResponseEntity.ok(Response(success = true, result = venues, error = null))
            } catch (e: Exception) {
                ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
            }

        @Operation(summary = "Insert a new venue")
        @PostMapping("")
        fun insertVenue(
            @RequestBody addedVenue: Venue,
        ): ResponseEntity<Response<Venue>> =
            try {
                val venue = venueService.insertVenue(addedVenue)
                ResponseEntity.ok(Response(success = true, result = venue, error = null))
            } catch (e: Exception) {
                ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
            }

        @Operation(summary = "Remove venue by id")
        @DeleteMapping("/{id}")
        fun removeVenueById(
            @PathVariable id: String,
        ): ResponseEntity<Response<Boolean>> =
            try {
                venueService.deleteClass(id)
                ResponseEntity.ok(Response(success = true, result = true, error = null))
            } catch (e: Exception) {
                ResponseEntity.badRequest().body(Response(success = false, result = false, error = e.message))
            }

        @PostMapping("/reservation")
        fun reserveVenue(
            @RequestBody reservation: ReservationDTO,
        ): ResponseEntity<Response<Boolean>> =
            try {
                venueService.reserveVenue(classService.findClassById(reservation.classId), reservation.venueId)
                ResponseEntity.ok(Response(success = true, result = true, error = null))
            } catch (e: Exception) {
                println(e)
                ResponseEntity.badRequest().body(Response(success = false, result = false, error = e.message))
            }
    }
