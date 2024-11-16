package com.rooplor.classcraftbackend.dtos

import com.rooplor.classcraftbackend.entities.FormField

data class FormCreateDTO(
    var classroomId: String = "",
    var title: String = "",
    var description: String = "",
    var openDate: String = "",
    var closeDate: String = "",
    var fields: List<FormField> = emptyList(),
)
