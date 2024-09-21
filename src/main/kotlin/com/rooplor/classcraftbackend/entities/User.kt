package com.rooplor.classcraftbackend.entities

import jakarta.validation.constraints.Email
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "user")
data class User(
    @Id
    var id: String? = null,
    var username: String = "",
    @field:Email
    var email: String = "",
    var profilePicture: String? = null,
)
