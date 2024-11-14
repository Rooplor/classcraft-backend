package com.rooplor.classcraftbackend.controllers

import com.rooplor.classcraftbackend.dtos.Response
import com.rooplor.classcraftbackend.entities.Form
import com.rooplor.classcraftbackend.entities.FormSubmission
import com.rooplor.classcraftbackend.helpers.FormHelper
import com.rooplor.classcraftbackend.services.FormService
import com.rooplor.classcraftbackend.services.FormSubmissionService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/form")
class FormController(
    private val formService: FormService,
    private val formSubmissionService: FormSubmissionService,
    private val formHelper: FormHelper,
) {
    @Operation(summary = "Create a form")
    @PostMapping("")
    fun createForm(
        @RequestBody form: Form,
    ): ResponseEntity<Response<Form>> {
        try {
            val createdForm = formService.createForm(form)
            return ResponseEntity.ok(Response(success = true, result = createdForm, error = null))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
        }
    }

    @Operation(summary = "Update a form")
    @PostMapping("/{id}")
    fun updateForm(
        @RequestBody form: Form,
        @PathVariable id: String,
    ): ResponseEntity<Response<Form>> {
        try {
            val updatedForm = formService.updateForm(id, form)
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
    @PostMapping("/{id}/submit")
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

    @Operation(summary = "Get form submissions by id")
    @GetMapping("/{id}/submissions")
    fun getFormSubmissions(
        @PathVariable id: String,
    ): ResponseEntity<Response<FormSubmission>> {
        try {
            val submissions = formSubmissionService.getFormSubmissionsById(id)
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