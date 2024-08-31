package com.rooplor.classcraftbackend.controllers

import com.rooplor.classcraftbackend.entities.Venue
import com.rooplor.classcraftbackend.services.VenueService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
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
        val service: VenueService,
    ) {
        @Operation(summary = "Get all venues")
        @GetMapping("")
        fun findAll(): List<Venue> = service.findAllVenue()

        @Operation(summary = "Get venue by id")
        @PostMapping("")
        fun insertVenue(
            @RequestBody addedVenue: Venue,
        ): Venue = service.insertVenue(addedVenue)

        @Operation(summary = "Remove venue by id")
        @DeleteMapping("/{id}")
        fun removeVenueById(
            @PathVariable id: String,
        ) = service.deleteClass(id)
    }
