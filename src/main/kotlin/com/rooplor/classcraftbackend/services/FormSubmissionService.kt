package com.rooplor.classcraftbackend.services

import com.google.zxing.BarcodeFormat
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.qrcode.QRCodeWriter
import com.opencsv.CSVWriter
import com.rooplor.classcraftbackend.dtos.UserDetailDTO
import com.rooplor.classcraftbackend.entities.FormSubmission
import com.rooplor.classcraftbackend.enums.AttendeesStatus
import com.rooplor.classcraftbackend.messages.ErrorMessages
import com.rooplor.classcraftbackend.repositories.FormSubmissionRepository
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
) {
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
        formSubmission.attendeesStatus = AttendeesStatus.PENDING

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
    ): FormSubmission {
        val formSubmission = formSubmissionRepository.findById(formSubmissionId).orElseThrow { Exception(ErrorMessages.ANSWER_NOT_FOUND) }
        formSubmission.attendeesStatus = attendeesStatus
        return formSubmissionRepository.save(formSubmission)
    }

    fun generateQRCodeWithLogo(barcode: String, logoPath: String): BufferedImage {
        val barcodeWriter = QRCodeWriter()
        val bitMatrix = barcodeWriter.encode(barcode, BarcodeFormat.QR_CODE, 500, 500)
        val qrCodeGrayscale = MatrixToImageWriter.toBufferedImage(bitMatrix)

        // Create a new BufferedImage with a color model
        val qrCode = BufferedImage(qrCodeGrayscale.width, qrCodeGrayscale.height, BufferedImage.TYPE_INT_ARGB)
        val g2d: Graphics2D = qrCode.createGraphics()
        g2d.drawImage(qrCodeGrayscale, 0, 0, null)
        g2d.dispose()

        // Load the logo image
        val logo = ImageIO.read(File(logoPath))

        // Calculate the position to place the logo
        val deltaHeight = qrCode.height - logo.height
        val deltaWidth = qrCode.width - logo.width

        // Draw the logo onto the QR code
        val g: Graphics2D = qrCode.createGraphics()
        g.drawImage(logo, deltaWidth / 2, deltaHeight / 2, null)
        g.dispose()

        return qrCode
    }
}
