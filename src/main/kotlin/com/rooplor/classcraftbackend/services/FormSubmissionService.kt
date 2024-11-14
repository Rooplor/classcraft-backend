package com.rooplor.classcraftbackend.services

import com.opencsv.CSVWriter
import com.rooplor.classcraftbackend.entities.FormSubmission
import com.rooplor.classcraftbackend.messages.ErrorMessages
import com.rooplor.classcraftbackend.repositories.FormRepository
import com.rooplor.classcraftbackend.repositories.FormSubmissionRepository
import org.springframework.stereotype.Service
import java.io.StringWriter

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

    fun generateCsvFromForm(classroomId: String): String {
        val submission = getFormSubmissionsByClassroomId(classroomId)
        val writer = StringWriter()
        val csvWriter = CSVWriter(writer)
        // Add csv header
        val header =
            submission
                .first()
                .responses.keys
                .toTypedArray()
        csvWriter.writeNext(header)
        // Add csv data
        submission.forEach {
            val data = it.responses.values.toTypedArray()
            csvWriter.writeNext(data.map { it.toString() }.toTypedArray())
        }
        csvWriter.close()
        return writer.toString()
    }
}
