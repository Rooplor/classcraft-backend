package com.rooplor.classcraftbackend.dtos

import com.rooplor.classcraftbackend.enums.ClassType
import com.rooplor.classcraftbackend.enums.Format
import java.time.LocalDateTime

data class InitClassDTO(
    var title: String,
    var details: String,
    var target: String,
    var prerequisite: String? = "",
    var type: ClassType,
    var format: Format,
    var capacity: Int,
    var date: List<LocalDateTime>,
    var instructorName: String = "",
    var instructorBio: String = "",
    var instructorAvatar: String = "",
    var instructorFamiliarity: String = "",
    var coverImage: String? = "",
    var coOwners: List<String>? = emptyList(),
)
