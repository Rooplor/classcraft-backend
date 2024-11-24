package com.rooplor.classcraftbackend.repositories

import com.rooplor.classcraftbackend.entities.Form
import org.springframework.data.mongodb.repository.MongoRepository

interface FormRepository : MongoRepository<Form, String> {
    fun findByClassroomId(classroomId: String): Form?
}
