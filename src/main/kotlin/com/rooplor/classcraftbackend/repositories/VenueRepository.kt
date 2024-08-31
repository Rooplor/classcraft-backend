package com.rooplor.classcraftbackend.repositories

import com.rooplor.classcraftbackend.entities.Venue
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface VenueRepository : MongoRepository<Venue, String> {
    override fun findAll(): List<Venue>

    override fun findById(id: String): Optional<Venue>
}
