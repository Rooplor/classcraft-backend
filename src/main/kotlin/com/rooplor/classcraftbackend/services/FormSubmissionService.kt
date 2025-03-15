package com.rooplor.classcraftbackend.services

import com.google.zxing.BarcodeFormat
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.qrcode.QRCodeWriter
import com.opencsv.CSVWriter
import com.rooplor.classcraftbackend.dtos.UserDetailDTO
import com.rooplor.classcraftbackend.entities.FormField
import com.rooplor.classcraftbackend.entities.FormSubmission
import com.rooplor.classcraftbackend.enums.AttendeesStatus
import com.rooplor.classcraftbackend.messages.ErrorMessages
import com.rooplor.classcraftbackend.repositories.FormSubmissionRepository
import com.rooplor.classcraftbackend.types.Attendees
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.File
import java.io.StringWriter
import javax.imageio.ImageIO

@Service
class FormSubmissionService(
    private val formSubmissionRepository: FormSubmissionRepository,
    private val formService: FormService,
    private val authService: AuthService,
    private val userService: UserService,
    private val classService: ClassService,
) {
    @Value("\${staff.domain}")
    private val domain: String? = null

    fun submitForm(formSubmission: FormSubmission): FormSubmission {
        val userId = authService.getUserId()
        val existingSubmission = formSubmissionRepository.findByFormIdAndSubmittedBy(formSubmission.formId, userId)
        if (existingSubmission != null) {
            throw Exception(ErrorMessages.ANSWER_ALREADY_SUBMITTED)
        }
        val userDetail = userService.findUserById(userId)
        formSubmission.submittedBy = userId
        formSubmission.userDetail =
            UserDetailDTO(
                id = userDetail.id!!,
                username = userDetail.username,
                profilePicture = userDetail.profilePicture,
            )
        val form = formService.findByClassroomId(formSubmission.classroomId)
        val allQuestion = form.fields.toSet()
        validateRequiredFields(allQuestion, formSubmission.responses)
        validateFormFields(form.fields, formSubmission.responses)

        formSubmission.isApprovedByOwner = !form.isOwnerApprovalRequired
        formSubmission.attendeesStatus = createAttendeesList(formSubmission.classroomId)

        return formSubmissionRepository.save(formSubmission)
    }

    fun submitFeedback(
        formSubmissionId: String,
        feedbackResponse: Map<String, Any>,
    ): FormSubmission {
        val formSubmission = formSubmissionRepository.findById(formSubmissionId).orElseThrow { Exception(ErrorMessages.ANSWER_NOT_FOUND) }
        val existingSubmission = formSubmission.feedbackResponse
        if (existingSubmission != null) {
            throw Exception(ErrorMessages.ANSWER_ALREADY_SUBMITTED)
        }
        val form = formService.findByClassroomId(formSubmission.classroomId)
        val allFeedbackQuestion = form.feedback?.toSet()
        validateRequiredFields(allFeedbackQuestion!!, feedbackResponse)
        validateFormFields(form.feedback!!, feedbackResponse)
        formSubmission.feedbackResponse = feedbackResponse
        return formSubmissionRepository.save(formSubmission)
    }

    fun getFormSubmissionByFormIdAndSubmittedBy(
        formId: String,
        submittedBy: String,
    ): FormSubmission? = formSubmissionRepository.findByFormIdAndSubmittedBy(formId, submittedBy)

    fun getFormSubmissionsByClassroomId(classroomId: String): List<FormSubmission> = formSubmissionRepository.findByClassroomId(classroomId)

    fun getUserInClassroom(classroomId: String): List<UserDetailDTO?> {
        val submissions = getFormSubmissionsByClassroomId(classroomId)
        val approvedByOwner = submissions.filter { it.isApprovedByOwner }
        return approvedByOwner.map { it.userDetail }
    }

    fun generateCsvFromForm(classroomId: String): String {
        val allAnswer = formService.findByClassroomId(classroomId)
        val submission = getFormSubmissionsByClassroomId(classroomId)
        val writer = StringWriter()
        val csvWriter = CSVWriter(writer)
        // Add csv header
        val header =
            arrayOf("No.") +
                allAnswer.fields.map { it.name }.toTypedArray() + "Registration Status" + "Attendees Status"
        csvWriter.writeNext(header)
        // Add csv data
        submission.forEachIndexed { index, formSubmission ->
            val data =
                arrayOf((index + 1).toString()) +
                    allAnswer.fields
                        .map { field ->
                            formSubmission.responses[field.name]?.toString() ?: ""
                        }.toTypedArray() + formSubmission.isApprovedByOwner.toString() + formSubmission.attendeesStatus.toString()
            csvWriter.writeNext(data)
        }
        csvWriter.close()
        return writer.toString()
    }

    fun setFormSubmissionApprovalStatus(
        formSubmissionId: String,
        isApproved: Boolean,
    ): FormSubmission {
        val formSubmission = formSubmissionRepository.findById(formSubmissionId).orElseThrow { Exception(ErrorMessages.ANSWER_NOT_FOUND) }
        formSubmission.isApprovedByOwner = isApproved
        return formSubmissionRepository.save(formSubmission)
    }

    fun getFormSubmissionByUserId(userId: String): List<FormSubmission> = formSubmissionRepository.findBySubmittedBy(userId)

    fun setAttendeesStatus(
        formSubmissionId: String,
        attendeesStatus: AttendeesStatus,
        day: Int,
    ): FormSubmission {
        val formSubmission = formSubmissionRepository.findById(formSubmissionId).orElseThrow { Exception(ErrorMessages.ANSWER_NOT_FOUND) }
        formSubmission.attendeesStatus =
            formSubmission.attendeesStatus?.map {
                if (it.day == day) {
                    it.copy(attendeesStatus = attendeesStatus)
                } else {
                    it
                }
            }
        return formSubmissionRepository.save(formSubmission)
    }

    fun generateQRCodeWithLogo(
        classId: String,
        logoPath: String,
    ): BufferedImage {
        val classroom = classService.findClassById(classId)
        if (classroom == null) {
            throw Exception("Cannot generate QR code: " + ErrorMessages.CLASS_NOT_FOUND)
        } else {
            val barcodeWriter = QRCodeWriter()
            val bitMatrix = barcodeWriter.encode("$domain/class/$classId/checkin", BarcodeFormat.QR_CODE, 500, 500)
            val qrCodeGrayscale = MatrixToImageWriter.toBufferedImage(bitMatrix)

            val qrCode = BufferedImage(qrCodeGrayscale.width, qrCodeGrayscale.height, BufferedImage.TYPE_INT_ARGB)
            val g2d: Graphics2D = qrCode.createGraphics()
            g2d.drawImage(qrCodeGrayscale, 0, 0, null)
            g2d.dispose()

            val logo = ImageIO.read(File(logoPath))

            val deltaHeight = qrCode.height - logo.height
            val deltaWidth = qrCode.width - logo.width

            val g: Graphics2D = qrCode.createGraphics()
            g.drawImage(logo, deltaWidth / 2, deltaHeight / 2, null)
            g.dispose()

            return qrCode
        }
    }

    private fun createAttendeesList(classroomId: String): List<Attendees> {
        val classroom = classService.findClassById(classroomId)
        val attendees = mutableListOf<Attendees>()
        var idCounter = 1
        classroom.dates.forEach { data ->
            attendees.add(
                Attendees(
                    day = idCounter++,
                    date = data.date.startDateTime.toLocalDate(),
                    attendeesStatus = AttendeesStatus.PENDING,
                ),
            )
        }
        return attendees
    }

    private fun validateFormFields(
        formField: List<FormField>,
        responses: Map<String, Any>,
    ) {
        formField.forEach { field ->
            val response = responses[field.name]
            if (response != null && field.validation != null) {
                val regex = field.validation!!.regex
                if (!regex.matches(response.toString())) {
                    throw Exception(ErrorMessages.FIELD_VALIDATE_FAIL.replace("$0", field.name))
                }
            }
        }
    }

    private fun validateRequiredFields(
        allQuestion: Set<FormField>,
        responses: Map<String, Any>,
    ) {
        val expectedQuestions = allQuestion.filter { it.required }.map { it.name }.toSet()
        val submissionForm = responses.map { it.key }.toSet()
        val diff = expectedQuestions - submissionForm
        if (diff.isNotEmpty()) {
            throw Exception(ErrorMessages.MISSING_REQUIRED_FIELDS.replace("$0", diff.joinToString(", ")))
        }
    }
}
