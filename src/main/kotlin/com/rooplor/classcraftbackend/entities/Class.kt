package com.rooplor.classcraftbackend.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "class")
data class Class(
    @Id
    val id: String? = null,
    val title: String,
    val imgUrl: String,
)

// [{
//    classId: int,
//    title: string,
//    imgUrl: string
// }]
