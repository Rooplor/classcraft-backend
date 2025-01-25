package com.rooplor.classcraftbackend.services

import com.opencsv.CSVWriter
import com.rooplor.classcraftbackend.entities.FormSubmission
import com.rooplor.classcraftbackend.messages.ErrorMessages
import com.rooplor.classcraftbackend.repositories.FormSubmissionRepository
import org.springframework.stereotype.Service
import java.io.StringWriter

@Service
class FormSubmissionService(
    private val formSubmissionRepository: FormSubmissionRepository,
    private val formService: FormService,
    private val authService: AuthService,
) {
    fun submitForm(formSubmission: FormSubmission): FormSubmission {
        val userId = authService.getUserId()
        val existingSubmission = formSubmissionRepository.findByFormIdAndSubmittedBy(formSubmission.formId, userId)
        if (existingSubmission != null) {
            throw Exception(ErrorMessages.ANSWER_ALREADY_SUBMITTED)
        }
        formSubmission.submittedBy = userId
        val form = formService.findByClassroomId(formSubmission.classroomId)
        val allQuestion = form.fields.toSet()
        val expectedQuestions = allQuestion.filter { it.required }.map { it.name }.toSet()
        val submissionForm = formSubmission.responses.map { it.key }.toSet()
        val diff = expectedQuestions - submissionForm
        if (diff.isNotEmpty()) {
            throw Exception(ErrorMessages.MISSING_REQUIRED_FIELDS.replace("$0", diff.joinToString(", ")))
        }

        form.fields.forEach { field ->
            val response = formSubmission.responses[field.name]
            if (response != null && field.validation != null) {
                val regex = field.validation!!.regex
                if (!regex.matches(response.toString())) {
                    throw Exception(ErrorMessages.FIELD_VALIDATE_FAIL.replace("$0", field.name))
                }
            }
        }

        formSubmission.isApprovedByOwner = !form.isOwnerApprovalRequired

        return formSubmissionRepository.save(formSubmission)
    }

    fun getFormSubmissionByFormIdAndSubmittedBy(
        formId: String,
        submittedBy: String,
    ): FormSubmission =
        formSubmissionRepository.findByFormIdAndSubmittedBy(formId, submittedBy) ?: throw Exception(ErrorMessages.ANSWER_NOT_FOUND)

    fun getFormSubmissionsByClassroomId(classroomId: String): List<FormSubmission> = formSubmissionRepository.findByClassroomId(classroomId)

    fun generateCsvFromForm(classroomId: String): String {
        val allAnswer = formService.findByClassroomId(classroomId)
        val submission = getFormSubmissionsByClassroomId(classroomId)
        val writer = StringWriter()
        val csvWriter = CSVWriter(writer)
        // Add csv header
        val header =
            arrayOf("No.") +
                allAnswer.fields.map { it.name }.toTypedArray()
        csvWriter.writeNext(header)
        // Add csv data
        submission.forEachIndexed { index, formSubmission ->
            val data =
                arrayOf((index + 1).toString()) +
                    allAnswer.fields
                        .map { field ->
                            formSubmission.responses[field.name]?.toString() ?: ""
                        }.toTypedArray()
            csvWriter.writeNext(data)
        }
        csvWriter.close()
        return writer.toString()
    }
}
