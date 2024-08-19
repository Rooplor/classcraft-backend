package com.rooplor.classcraftbackend.repositories

import com.rooplor.classcraftbackend.entities.Class
import org.springframework.data.mongodb.repository.MongoRepository

interface ClassRepository : MongoRepository<Class, String> {
    override fun findAll(): List<Class>
}
