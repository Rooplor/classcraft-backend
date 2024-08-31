package com.rooplor.classcraftbackend.entities

import com.rooplor.classcraftbackend.enums.VenueName
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "venue")
class Venue {
    @Id
    var id: String? = null
    var name: VenueName? = null
}
