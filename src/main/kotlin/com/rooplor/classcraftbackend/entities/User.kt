package com.rooplor.classcraftbackend.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "user")
data class User(
    @Id
    var id: String? = null,
    var username: String = "",
    var email: String = "",
    var profilePicture: String? = null,
)
