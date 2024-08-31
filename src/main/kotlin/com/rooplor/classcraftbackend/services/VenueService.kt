package com.rooplor.classcraftbackend.services

import com.rooplor.classcraftbackend.entities.Venue
import com.rooplor.classcraftbackend.repositories.VenueRepository
import lombok.AllArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@AllArgsConstructor
@Service
class VenueService
    @Autowired
    constructor(
        private val venueRepository: VenueRepository,
    ) {
        fun findAllVenue(): List<Venue> = venueRepository.findAll()

        fun insertVenue(addedVenue: Venue): Venue = venueRepository.insert(addedVenue)

        fun findVenueById(id: String): Venue = venueRepository.findById(id).orElseThrow()

        fun deleteClass(id: String) = venueRepository.deleteById(id)
    }
