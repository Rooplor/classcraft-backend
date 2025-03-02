package com.rooplor.classcraftbackend.entities

import com.rooplor.classcraftbackend.dtos.UserDetailDTO
import com.rooplor.classcraftbackend.types.Attendees
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "formSubmissions")
data class FormSubmission(
    @Id
    var id: String? = null,
    var formId: String,
    var classroomId: String,
    var responses: Map<String, Any>,
    var submittedBy: String? = null,
    var userDetail: UserDetailDTO? = null,
    var isApprovedByOwner: Boolean = false,
    var attendeesStatus: List<Attendees>? = emptyList(),
)
