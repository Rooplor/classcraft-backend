package com.rooplor.classcraftbackend.dtos

data class FormSubmissionDTO(
    var formId: String,
    var classroomId: String,
    var responses: Map<String, Any>,
)
