package com.rooplor.classcraftbackend.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "form")
data class Form(
    @Id
    var id: String? = null,
    var classroomId: String,
    var title: String,
    var description: String,
    var openDate: String,
    var closeDate: String,
    var fields: List<FormField>,
)

data class FormField(
    var name: String,
    var type: String,
    var required: Boolean,
    var validation: Map<String, Any>?,
    var options: List<String>?,
)
