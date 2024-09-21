package com.rooplor.classcraftbackend.repositories

import com.rooplor.classcraftbackend.entities.User
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface UserRepository : MongoRepository<User, String> {
    override fun findAll(): List<User>

    override fun findById(id: String): Optional<User>

    fun findByEmail(email: String): Optional<User>

    fun findByUsername(username: String): Optional<User>
}
