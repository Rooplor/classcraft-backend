package com.rooplor.classcraftbackend.repositories

import com.rooplor.classcraftbackend.entities.Classroom
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface ClassRepository : MongoRepository<Classroom, String> {
    override fun findAll(): List<Classroom>

    override fun findById(id: String): Optional<Classroom>
}
