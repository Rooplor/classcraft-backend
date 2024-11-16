package com.rooplor.classcraftbackend.services

import com.rooplor.classcraftbackend.entities.Form
import com.rooplor.classcraftbackend.messages.ErrorMessages
import com.rooplor.classcraftbackend.repositories.FormRepository
import org.springframework.stereotype.Service

@Service
class FormService(
    private val formRepository: FormRepository,
) {
    fun createForm(form: Form): Form = formRepository.insert(form)

    fun getFormById(id: String): Form = formRepository.findById(id).orElseThrow({ Exception(ErrorMessages.FORM_NOT_FOUND) })

    fun updateForm(
        id: String,
        updatedForm: Form,
    ): Form {
        val formToUpdate = getFormById(id)
        formToUpdate.classroomId = updatedForm.classroomId
        formToUpdate.title = updatedForm.title
        formToUpdate.description = updatedForm.description
        formToUpdate.openDate = updatedForm.openDate
        formToUpdate.closeDate = updatedForm.closeDate
        formToUpdate.fields = updatedForm.fields
        return formRepository.save(formToUpdate)
    }

    fun findByClassroomId(classroomId: String): Form =
        formRepository.findByClassroomId(classroomId) ?: throw Exception(ErrorMessages.FORM_NOT_FOUND)

    fun deleteFormById(id: String) = formRepository.deleteById(id)
}
