package com.rooplor.classcraftbackend.repositories

import com.rooplor.classcraftbackend.entities.Classroom
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface ClassroomRepository : MongoRepository<Classroom, String> {
    override fun findAll(): List<Classroom>

    override fun findById(id: String): Optional<Classroom>

    fun findByRegistrationStatusAndIsPublishedTrueOrderByCreatedWhen(status: Boolean): List<Classroom>

    fun findByOwner(owners: String): List<Classroom>
}
