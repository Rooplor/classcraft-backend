package com.rooplor.classcraftbackend.helpers

import com.rooplor.classcraftbackend.dtos.FormCreateDTO
import com.rooplor.classcraftbackend.entities.FormField
import org.springframework.stereotype.Component

@Component
class FormHelper {
    fun mergeListOfMaps(list: List<Map<String, String>>): Map<String, String> =
        list.fold(mutableMapOf()) { acc, map ->
            acc.putAll(map)
            acc
        }

    fun validateFormField(field: FormField): List<String> {
        val errors = mutableListOf<String>()

        if (field.name.isBlank()) {
            errors.add("Field name is mandatory")
        }
        if (field.type.isBlank()) {
            errors.add("Field type is mandatory")
        }

        return errors
    }

    fun validateForm(form: FormCreateDTO): List<String> {
        val errors = mutableListOf<String>()

        if (form.classroomId.isBlank()) {
            errors.add("Classroom ID is mandatory")
        }
        if (form.title.isBlank()) {
            errors.add("Title is mandatory")
        }
        if (form.description.isBlank()) {
            errors.add("Description is mandatory")
        }
        if (form.fields.isEmpty()) {
            errors.add("Fields are mandatory")
        } else {
            form.fields.forEachIndexed { index, field ->
                val fieldErrors = validateFormField(field)
                if (fieldErrors.isNotEmpty()) {
                    errors.add("Field $index: ${fieldErrors.joinToString(", ")}")
                }
            }
        }

        return errors
    }
}
