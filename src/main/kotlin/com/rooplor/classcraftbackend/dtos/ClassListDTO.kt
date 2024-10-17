package com.rooplor.classcraftbackend.dtos

import com.rooplor.classcraftbackend.enums.ClassType
import com.rooplor.classcraftbackend.enums.Format
import lombok.NoArgsConstructor
import java.time.LocalDateTime

@NoArgsConstructor
data class ClassListDTO(
    var id: String? = "",
    var title: String? = "",
    var details: String? = "",
    var type: ClassType? = ClassType.LECTURE,
    var format: Format? = Format.ONSITE,
    var capacity: Int? = 0,
    var date: List<LocalDateTime?> = listOf(),
    var registrationUrl: String? = "",
    var owners: List<String> = listOf(),
)
