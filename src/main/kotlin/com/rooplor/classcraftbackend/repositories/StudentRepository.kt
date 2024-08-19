package com.rooplor.classcraftbackend.repositories

import com.rooplor.classcraftbackend.entities.Student
import org.springframework.data.mongodb.repository.MongoRepository

interface StudentRepository : MongoRepository<Student, String>
