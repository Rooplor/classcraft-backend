package com.rooplor.classcraftbackend.dtos

import com.rooplor.classcraftbackend.entities.FormField
import java.time.LocalDateTime

data class FormCreateDTO(
    var classroomId: String = "",
    var title: String = "",
    var description: String = "",
    var openDate: LocalDateTime? = null,
    var closeDate: LocalDateTime? = null,
    var fields: List<FormField> = emptyList(),
)
