package com.rooplor.classcraftbackend.entities

import com.rooplor.classcraftbackend.enums.FieldValidation
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "form")
data class Form(
    @Id
    var id: String? = null,
    var classroomId: String = "",
    var title: String = "",
    var description: String = "",
    var openDate: LocalDateTime? = null,
    var closeDate: LocalDateTime? = null,
    var fields: List<FormField> = emptyList(),
    var isOwnerApprovalRequired: Boolean = false,
)

data class FormField(
    var name: String = "",
    var type: String = "",
    var required: Boolean = false,
    var validation: FieldValidation? = null,
    var options: List<String>? = emptyList(),
)
