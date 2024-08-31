package com.rooplor.classcraftbackend.controllers

import com.rooplor.classcraftbackend.entities.Venue
import com.rooplor.classcraftbackend.services.VenueService
import com.rooplor.classcraftbackend.utils.ListMapper
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/venue")
class
VenueController@Autowired
    constructor(
        val service: VenueService,
        val modelMapper: ModelMapper,
        val listMapper: ListMapper,
    ) {
        @GetMapping("")
        fun findAll(): List<Venue> = service.findAllVenue()

        @PostMapping("")
        fun insertVenue(
            @RequestBody addedVenue: Venue,
        ): Venue = service.insertVenue(addedVenue)
    }
