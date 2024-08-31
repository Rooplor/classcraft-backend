package com.rooplor.classcraftbackend.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "venue")
data class Venue(
    @Id
    var id: String? = null,
    var name: String? = null,
)
