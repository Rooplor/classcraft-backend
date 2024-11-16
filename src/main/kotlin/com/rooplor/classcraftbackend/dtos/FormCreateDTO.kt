package com.rooplor.classcraftbackend.dtos

import com.rooplor.classcraftbackend.entities.FormField
import java.time.LocalDateTime

data class FormCreateDTO(
    var classroomId: String = "",
    var title: String = "",
    var description: String = "",
    var openDate: LocalDateTime = LocalDateTime.now(),
    var closeDate: LocalDateTime = LocalDateTime.now(),
    var fields: List<FormField> = emptyList(),
)
