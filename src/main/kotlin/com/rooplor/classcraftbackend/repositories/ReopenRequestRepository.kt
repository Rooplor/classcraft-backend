package com.rooplor.classcraftbackend.repositories

import com.rooplor.classcraftbackend.entities.ReopenRequest
import org.springframework.data.mongodb.repository.MongoRepository

interface ReopenRequestRepository : MongoRepository<ReopenRequest, String> {
    fun findByClassroomId(classroomId: String): ReopenRequest?

    fun findByOwnerId(ownerId: String): List<ReopenRequest>

    fun deleteByClassroomId(classroomId: String)

    fun existsByClassroomId(classroomId: String): Boolean
}
