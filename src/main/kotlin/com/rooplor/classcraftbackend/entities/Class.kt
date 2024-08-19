package com.rooplor.classcraftbackend.entities

import com.rooplor.classcraftbackend.enums.Format
import com.rooplor.classcraftbackend.enums.Status
import com.rooplor.classcraftbackend.enums.Type
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "class")
data class Class(
    @Id
    val id: String? = null,
    val title: String,
    val details: String,
    val target: String,
    val prerequisite: String,
    val type: Type,
    val format: Format,
    val capacity: Short,
    val date: List<LocalDateTime>,
    val stepperStatus: Status? = Status.FILL_CASS_DETAIL,
    val meetingUrl: String? = null,
    val venue: String? = null,
    val venueStatus: String? = null,
    val content: String? = null,
    val registrationUrl: String? = null,
    val registrationStatus: Boolean? = false,
    val isPublished: Boolean? = false,
)
