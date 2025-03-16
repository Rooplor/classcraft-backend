package com.rooplor.classcraftbackend.services

import com.rooplor.classcraftbackend.entities.Form
import com.rooplor.classcraftbackend.entities.FormField
import com.rooplor.classcraftbackend.enums.FieldValidation
import com.rooplor.classcraftbackend.helpers.FormHelper
import com.rooplor.classcraftbackend.messages.ErrorMessages
import com.rooplor.classcraftbackend.repositories.FormRepository
import com.rooplor.classcraftbackend.repositories.FormSubmissionRepository
import org.springframework.stereotype.Service

@Service
class FormService(
    private val formRepository: FormRepository,
    private val formHelper: FormHelper,
    private val formSubmissionRepository: FormSubmissionRepository,
) {
    fun createForm(form: Form): Form {
        formHelper.validateForm(form)
        form.id = form.classroomId
        initDefaultFormQuestions(form)
        initDefaultFormFeedback(form)
        return formRepository.insert(form)
    }

    fun getFormById(id: String): Form = formRepository.findById(id).orElseThrow({ Exception(ErrorMessages.FORM_NOT_FOUND) })

    fun updateForm(
        id: String,
        updatedForm: Form,
    ): Form {
        formHelper.validateForm(updatedForm)
        val formToUpdate = getFormById(id)
        formToUpdate.classroomId = updatedForm.classroomId
        formToUpdate.title = updatedForm.title
        formToUpdate.description = updatedForm.description
        formToUpdate.openDate = updatedForm.openDate
        formToUpdate.closeDate = updatedForm.closeDate
        formToUpdate.fields = updatedForm.fields
        formToUpdate.isOwnerApprovalRequired = updatedForm.isOwnerApprovalRequired
        return formRepository.save(formToUpdate)
    }

    fun findByClassroomId(classroomId: String): Form =
        formRepository.findByClassroomId(classroomId) ?: throw Exception(ErrorMessages.FORM_NOT_FOUND)

    fun deleteFormById(id: String) {
        formRepository.deleteById(id)
        formSubmissionRepository.deleteByFormId(id)
    }

    fun deleteFormSubmissionByFormId(formId: String) {
        formSubmissionRepository.deleteByFormId(formId)
    }

    fun createFormFeedback(
        formId: String,
        feedBack: List<FormField>,
    ): Form {
        val form = getFormById(formId)
        form.feedback = feedBack
        return formRepository.save(form)
    }

    private fun initDefaultFormQuestions(form: Form) {
        val question =
            listOf(
                FormField(
                    name = "Full Name",
                    type = "text",
                    required = true,
                    validation = FieldValidation.TEXT,
                ),
                FormField(
                    name = "Email",
                    type = "email",
                    required = true,
                    validation = FieldValidation.EMAIL,
                ),
                FormField(
                    name = "Phone",
                    type = "tel",
                    required = false,
                    validation = FieldValidation.PHONE,
                ),
            )

        form.fields += question
    }

    private fun initDefaultFormFeedback(form: Form) {
        val feedback =
            listOf(
                FormField(
                    name = "Rating",
                    type = "number",
                    required = true,
                    validation = FieldValidation.RATE,
                ),
                FormField(
                    name = "Comments",
                    type = "textarea",
                    required = false,
                    validation = FieldValidation.TEXT,
                ),
            )

        form.feedback = form.feedback?.plus(feedback)
    }
}
