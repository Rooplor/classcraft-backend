package com.rooplor.classcraftbackend.helpers

import com.rooplor.classcraftbackend.entities.Form
import com.rooplor.classcraftbackend.entities.FormField
import org.springframework.stereotype.Component

@Component
class FormHelper {
    fun mergeListOfMaps(list: List<Map<String, String>>): Map<String, String> =
        list.fold(mutableMapOf()) { acc, map ->
            acc.putAll(map)
            acc
        }

    fun validateFormField(field: FormField): List<String> =
        listOf(
            field.name to "Field name is mandatory",
            field.type to "Field type is mandatory",
        ).mapNotNull { (value, errorMessage) ->
            if (value.isBlank()) errorMessage else null
        }

    fun validateForm(form: Form) {
        val errors = mutableListOf<String>()

        val formValidations =
            listOf(
                form.classroomId to "Classroom ID is mandatory",
                form.title to "Title is mandatory",
                form.description to "Description is mandatory",
            )

        formValidations.forEach { (value, errorMessage) ->
            if (value.isBlank()) {
                errors.add(errorMessage)
            }
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

        if (errors.isNotEmpty()) {
            throw Exception(errors.joinToString(", "))
        }
    }
}
