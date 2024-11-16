package com.rooplor.classcraftbackend.dtos

data class FormSubmissionDTO(
    val formId: String,
    val classroomId: String,
    val responses: Map<String, Any>,
)
