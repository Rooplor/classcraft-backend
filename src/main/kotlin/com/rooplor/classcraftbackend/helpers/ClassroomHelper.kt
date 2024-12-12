package com.rooplor.classcraftbackend.helpers

import com.rooplor.classcraftbackend.entities.Classroom
import org.springframework.stereotype.Component

@Component
class ClassroomHelper {
    fun validateClassroom(classroom: Classroom) {
        val errors = mutableListOf<String>()

        val validations =
            listOf(
                classroom.title to "Title is mandatory",
                classroom.details to "Details is mandatory",
                classroom.target to "Target is mandatory",
                classroom.instructorName to "Instructor name is mandatory",
                classroom.instructorBio to "Instructor bio is mandatory",
                classroom.instructorAvatar to "Instructor avatar is mandatory",
                classroom.instructorFamiliarity to "Instructor familiarity is mandatory",
            )

        validations.forEach { (value, errorMessage) ->
            if (value.isBlank()) {
                errors.add(errorMessage)
            }
        }

        if (classroom.capacity <= 0) {
            errors.add("Capacity must be greater than 0")
        }

        if (classroom.dates.isEmpty()) {
            errors.add("Dates are mandatory")
        }

        if (errors.isNotEmpty()) {
            throw Exception(errors.joinToString(", "))
        }
    }
}
