package com.rooplor.classcraftbackend.controllers

import com.rooplor.classcraftbackend.dtos.FormCreateDTO
import com.rooplor.classcraftbackend.dtos.Response
import com.rooplor.classcraftbackend.entities.Form
import com.rooplor.classcraftbackend.entities.FormSubmission
import com.rooplor.classcraftbackend.helpers.FormHelper
import com.rooplor.classcraftbackend.services.FormService
import com.rooplor.classcraftbackend.services.FormSubmissionService
import io.swagger.v3.oas.annotations.Operation
import org.modelmapper.ModelMapper
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

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
            val error = formHelper.validateForm(form)
            if (error.isNotEmpty()) {
                return ResponseEntity.badRequest().body(
                    Response(success = false, result = null, error = error.joinToString(", ")),
                )
            }
            val createdForm = formService.createForm(modelMapper.map(form, Form::class.java))
            return ResponseEntity.ok(Response(success = true, result = createdForm, error = null))
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
            val error = formHelper.validateForm(form)
            if (error.isNotEmpty()) {
                return ResponseEntity.badRequest().body(
                    Response(
                        success = false,
                        result = null,
                        error = error.joinToString(", "),
                    ),
                )
            }
            val updatedForm = formService.updateForm(id, modelMapper.map(form, Form::class.java))
            return ResponseEntity.ok(Response(success = true, result = updatedForm, error = null))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
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
            return ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
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
            return ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
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
        @RequestBody formSubmission: FormSubmission,
    ): ResponseEntity<Response<FormSubmission>> {
        try {
            val submittedForm = formSubmissionService.submitForm(formSubmission)
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
}
