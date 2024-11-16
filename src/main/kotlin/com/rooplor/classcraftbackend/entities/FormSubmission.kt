package com.rooplor.classcraftbackend.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "formSubmissions")
data class FormSubmission(
    @Id
    val id: String? = null,
    val formId: String = "",
    val classroomId: String = "",
    val responses: Map<String, Any> = emptyMap(),
)
