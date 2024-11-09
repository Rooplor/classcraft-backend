package com.rooplor.classcraftbackend.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "venue")
data class Venue(
    @Id
    var id: String? = null,
    var name: String = "",
    var location: location = location(),
    var description: String = "",
    var capacity: Number = 0,
    var imageUrl: String = "",
)

@Suppress("ktlint:standard:class-naming")
data class location(
    var building: String = "",
    var floor: Number = 0,
)
