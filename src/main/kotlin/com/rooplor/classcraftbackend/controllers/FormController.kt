package com.rooplor.classcraftbackend.controllers

import com.rooplor.classcraftbackend.dtos.FeedbackResponse
import com.rooplor.classcraftbackend.dtos.FormCreateDTO
import com.rooplor.classcraftbackend.dtos.FormSubmissionDTO
import com.rooplor.classcraftbackend.dtos.Response
import com.rooplor.classcraftbackend.dtos.UserDetailDTO
import com.rooplor.classcraftbackend.entities.Form
import com.rooplor.classcraftbackend.entities.FormField
import com.rooplor.classcraftbackend.entities.FormSubmission
import com.rooplor.classcraftbackend.enums.AttendeesStatus
import com.rooplor.classcraftbackend.helpers.FormHelper
import com.rooplor.classcraftbackend.messages.ErrorMessages
import com.rooplor.classcraftbackend.services.FormService
import com.rooplor.classcraftbackend.services.FormSubmissionService
import io.swagger.v3.oas.annotations.Operation
import org.modelmapper.ModelMapper
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.awt.image.BufferedImage

@RestController
@RequestMapping("/api/form")
class FormController(
    private val formService: FormService,
    private val formSubmissionService: FormSubmissionService,
    private val formHelper: FormHelper,
    private val modelMapper: ModelMapper,
) {
    @Operation(summary = "Create a form")
    @PostMapping("")
    fun createForm(
        @RequestBody form: FormCreateDTO,
    ): ResponseEntity<Response<Form>> {
        try {
            val createdForm = formService.createForm(modelMapper.map(form, Form::class.java))
            return ResponseEntity.status(HttpStatus.CREATED).body(Response(success = true, result = createdForm, error = null))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
        }
    }

    @Operation(summary = "Update a form")
    @PutMapping("/{id}")
    fun updateForm(
        @RequestBody form: FormCreateDTO,
        @PathVariable id: String,
    ): ResponseEntity<Response<Form>> {
        try {
            val updatedForm = formService.updateForm(id, modelMapper.map(form, Form::class.java))
            return ResponseEntity.ok(Response(success = true, result = updatedForm, error = null))
        } catch (e: Exception) {
            return if (e.message == ErrorMessages.FORM_NOT_FOUND) {
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response(success = false, result = null, error = e.message))
            } else {
                ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
            }
        }
    }

    @Operation(summary = "Get a form by id")
    @GetMapping("/{id}")
    fun getFormById(
        @PathVariable id: String,
    ): ResponseEntity<Response<Form>> {
        try {
            val form = formService.getFormById(id)
            return ResponseEntity.ok(Response(success = true, result = form, error = null))
        } catch (e: Exception) {
            return if (e.message == ErrorMessages.FORM_NOT_FOUND) {
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response(success = false, result = null, error = e.message))
            } else {
                ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
            }
        }
    }

    @Operation(summary = "Get form questions by id")
    @GetMapping("/{id}/questions")
    fun getFormQuestionsById(
        @PathVariable id: String,
    ): ResponseEntity<Response<Map<String, String>>> {
        try {
            val form = formService.getFormById(id)
            val questions = formHelper.mergeListOfMaps(form.fields.map { mapOf(it.name to it.type) })
            return ResponseEntity.ok(Response(success = true, result = questions, error = null))
        } catch (e: Exception) {
            return if (e.message == ErrorMessages.FORM_NOT_FOUND) {
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response(success = false, result = null, error = e.message))
            } else {
                ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
            }
        }
    }

    @Operation(summary = "Delete a form by id")
    @DeleteMapping("/{id}")
    fun deleteFormById(
        @PathVariable id: String,
    ): ResponseEntity<Response<String>> {
        try {
            formService.deleteFormById(id)
            return ResponseEntity.ok(Response(success = true, result = "Form deleted", error = null))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
        }
    }

    @Operation(summary = "Submit a form")
    @PostMapping("/submit")
    fun submitForm(
        @RequestBody formSubmission: FormSubmissionDTO,
    ): ResponseEntity<Response<FormSubmission>> {
        try {
            val formToSubmit =
                FormSubmission(
                    formId = formSubmission.formId,
                    classroomId = formSubmission.classroomId,
                    responses = formSubmission.responses,
                )
            val submittedForm = formSubmissionService.submitForm(formToSubmit)
            return ResponseEntity.ok(Response(success = true, result = submittedForm, error = null))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
        }
    }

    @Operation(summary = "Get form submissions by form id and user id")
    @GetMapping("/submissions")
    fun getFormSubmissions(
        @RequestParam userId: String,
        @RequestParam formId: String,
    ): ResponseEntity<Response<FormSubmission>> {
        try {
            val submissions = formSubmissionService.getFormSubmissionByFormIdAndSubmittedBy(formId, userId)
            return ResponseEntity.ok(Response(success = true, result = submissions, error = null))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
        }
    }

    @Operation(summary = "Get form submissions by classroom id")
    @GetMapping("/submissions/{classroomId}")
    fun getFormSubmissionsByFormId(
        @PathVariable classroomId: String,
    ): ResponseEntity<Response<List<FormSubmission>>> {
        try {
            val submissions = formSubmissionService.getFormSubmissionsByClassroomId(classroomId)
            return ResponseEntity.ok(Response(success = true, result = submissions, error = null))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
        }
    }

    @Operation(summary = "Generate CSV from form")
    @GetMapping("/{id}/csv")
    fun generateCsvFromForm(
        @PathVariable id: String,
    ): ResponseEntity<ByteArray> {
        val csv = formSubmissionService.generateCsvFromForm(id)
        return try {
            ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"form_$id.csv\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csv.toByteArray())
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(ByteArray(0))
        }
    }

    @Operation(summary = "Set isApprovedByOwner status")
    @PatchMapping("/isApprovedByOwner/{id}")
    fun changeIsApprovedByOwner(
        @PathVariable id: String,
        @RequestParam isApproved: Boolean,
    ): ResponseEntity<Response<FormSubmission>> {
        try {
            val submission = formSubmissionService.setFormSubmissionApprovalStatus(id, isApproved)
            return ResponseEntity.ok(Response(success = true, result = submission, error = null))
        } catch (e: Exception) {
            return if (e.message == ErrorMessages.ANSWER_NOT_FOUND) {
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response(success = false, result = null, error = e.message))
            } else {
                ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
            }
        }
    }

    @Operation(summary = "Get form submissions by user id")
    @GetMapping("/submissions/user/{userId}")
    fun getFormSubmissionsByUserId(
        @PathVariable userId: String,
    ): ResponseEntity<Response<List<FormSubmission>>> {
        try {
            val submissions = formSubmissionService.getFormSubmissionByUserId(userId)
            return ResponseEntity.ok(Response(success = true, result = submissions, error = null))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
        }
    }

    @Operation(summary = "Set attendees status")
    @PatchMapping("/attendees/{id}")
    fun changeAttendeesStatus(
        @PathVariable id: String,
        @RequestParam day: Int,
        @RequestParam status: AttendeesStatus,
    ): ResponseEntity<Response<FormSubmission>> {
        try {
            val submission = formSubmissionService.setAttendeesStatus(id, status, day)
            return ResponseEntity.ok(Response(success = true, result = submission, error = null))
        } catch (e: Exception) {
            return if (e.message == ErrorMessages.ANSWER_NOT_FOUND) {
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response(success = false, result = null, error = e.message))
            } else {
                ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
            }
        }
    }

    @Operation(summary = "Get user in classroom")
    @GetMapping("/user/{classroomId}")
    fun getUserInClassroom(
        @PathVariable classroomId: String,
    ): ResponseEntity<Response<List<UserDetailDTO?>>> {
        try {
            val users = formSubmissionService.getUserInClassroom(classroomId)
            return ResponseEntity.ok(Response(success = true, result = users, error = null))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
        }
    }

    @GetMapping("/qrcode/checkin/{classId}", produces = [MediaType.IMAGE_PNG_VALUE])
    fun barbecueEAN13Barcode(
        @PathVariable classId: String,
        @RequestParam(required = false) day: Int? = null,
    ): ResponseEntity<BufferedImage> =
        try {
            ResponseEntity(
                formSubmissionService.generateQRCodeWithLogo(
                    classId,
                    "src/main/resources/classcraftlogo.png",
                    day,
                ),
                HttpStatus.OK,
            )
        } catch (e: Exception) {
            ResponseEntity(
                null,
                HttpStatus.BAD_REQUEST,
            )
        }

    @PatchMapping("/feedback/{formId}")
    fun createFormFeedBack(
        @PathVariable formId: String,
        @RequestBody feedBack: List<FormField>,
    ): ResponseEntity<Response<Form>> {
        try {
            val feedBackList = feedBack.map { modelMapper.map(it, FormField::class.java) }
            val form = formService.createFormFeedback(formId, feedBackList)
            return ResponseEntity.ok(Response(success = true, result = form, error = null))
        } catch (e: Exception) {
            return if (e.message == ErrorMessages.FORM_NOT_FOUND) {
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response(success = false, result = null, error = e.message))
            } else {
                ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
            }
        }
    }

    @PatchMapping("/feedback/submit/{formSubmissionId}")
    fun submitFormFeedBack(
        @PathVariable formSubmissionId: String,
        @RequestBody feedBack: Map<String, Any>,
    ): ResponseEntity<Response<FormSubmission>> {
        try {
            val form = formSubmissionService.submitFeedback(formSubmissionId, feedBack)
            return ResponseEntity.ok(Response(success = true, result = form, error = null))
        } catch (e: Exception) {
            return if (e.message == ErrorMessages.ANSWER_NOT_FOUND) {
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response(success = false, result = null, error = e.message))
            } else {
                ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
            }
        }
    }

    @GetMapping("/feedback/{classroomId}")
    fun getFormFeedBackByClassRoomId(
        @PathVariable classroomId: String,
    ): ResponseEntity<Response<List<FeedbackResponse>>> {
        try {
            val formSubmissions = formSubmissionService.getFormSubmissionsByClassroomId(classroomId)
            val response =
                formSubmissions.map {
                    FeedbackResponse(
                        userDetail = it.userDetail ?: UserDetailDTO(),
                        feedbackResponse = it.feedbackResponse ?: emptyMap(),
                    )
                }
            return ResponseEntity.ok(Response(success = true, result = response, error = null))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
        }
    }
}
