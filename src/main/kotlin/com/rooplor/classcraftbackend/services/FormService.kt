package com.rooplor.classcraftbackend.services

import com.rooplor.classcraftbackend.entities.Form
import com.rooplor.classcraftbackend.entities.FormField
import com.rooplor.classcraftbackend.messages.ErrorMessages
import com.rooplor.classcraftbackend.repositories.FormRepository
import org.springframework.stereotype.Service

@Service
class FormService(
    private val formRepository: FormRepository,
) {
    fun createForm(form: Form): Form {
        val errors = validateForm(form)
        if (errors.isNotEmpty()) {
            throw Exception(errors.joinToString(", "))
        }
        return formRepository.insert(form)
    }

    fun getFormById(id: String): Form = formRepository.findById(id).orElseThrow({ Exception(ErrorMessages.FORM_NOT_FOUND) })

    fun updateForm(
        id: String,
        updatedForm: Form,
    ): Form {
        val errors = validateForm(updatedForm)
        if (errors.isNotEmpty()) {
            throw Exception(errors.joinToString(", "))
        }
        val formToUpdate = getFormById(id)
        formToUpdate.classroomId = updatedForm.classroomId
        formToUpdate.title = updatedForm.title
        formToUpdate.description = updatedForm.description
        formToUpdate.openDate = updatedForm.openDate
        formToUpdate.closeDate = updatedForm.closeDate
        formToUpdate.fields = updatedForm.fields
        return formRepository.save(formToUpdate)
    }

    fun deleteFormById(id: String) = formRepository.deleteById(id)

    fun validateFormField(field: FormField): List<String> {
        val errors = mutableListOf<String>()

        if (field.name.isBlank()) {
            errors.add("Field name is mandatory")
        }
        if (field.type.isBlank()) {
            errors.add("Field type is mandatory")
        }
        if (field.required == null) {
            errors.add("Field required is mandatory")
        }

        return errors
    }

    fun validateForm(form: Form): List<String> {
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
        if (form.openDate.isBlank()) {
            errors.add("Open date is mandatory")
        }
        if (form.closeDate.isBlank()) {
            errors.add("Close date is mandatory")
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
