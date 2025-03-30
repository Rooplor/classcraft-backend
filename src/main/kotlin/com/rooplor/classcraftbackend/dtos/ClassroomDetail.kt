package com.rooplor.classcraftbackend.dtos

import com.rooplor.classcraftbackend.enums.ClassType
import com.rooplor.classcraftbackend.enums.Format

data class ClassroomDetail(
    var coverImage: String? = null,
    var title: String = "",
    var format: Format = Format.ONSITE,
    var type: ClassType = ClassType.LECTURE,
    var capacity: Int = 0,
    var instructorName: String = "",
    var instructorAvatar: String = "",
)
