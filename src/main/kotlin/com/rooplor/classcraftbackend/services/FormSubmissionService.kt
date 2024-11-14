package com.rooplor.classcraftbackend.services

import com.rooplor.classcraftbackend.entities.FormSubmission
import com.rooplor.classcraftbackend.messages.ErrorMessages
import com.rooplor.classcraftbackend.repositories.FormRepository
import com.rooplor.classcraftbackend.repositories.FormSubmissionRepository
import org.springframework.stereotype.Service

@Service
class FormSubmissionService(
    private val formSubmissionRepository: FormSubmissionRepository,
    private val formRepository: FormRepository,
) {
    fun submitForm(formSubmission: FormSubmission): FormSubmission {
        val form = formRepository.findByClassroomId(formSubmission.classroomId) ?: throw Exception("something went wrong")

        val allQuestion = form.fields.toSet()
        val expectedQuestions = allQuestion.filter { it.required }.map { it.name }.toSet()
        val submissionForm = formSubmission.responses.map { it.key }.toSet()
        val diff = expectedQuestions - submissionForm
        if (diff.isNotEmpty()) {
            throw Exception(ErrorMessages.MISSING_REQUIRED_FIELDS.replace("$0", diff.joinToString(", ")))
        }

        return formSubmissionRepository.save(formSubmission)
    }

    fun getFormSubmissionsById(id: String): FormSubmission =
        formSubmissionRepository.findById(id).orElseThrow({
            Exception(ErrorMessages.ANSWER_NOT_FOUND)
        })

    fun getFormSubmissionsByClassroomId(classroomId: String): List<FormSubmission> = formSubmissionRepository.findByClassroomId(classroomId)
}
