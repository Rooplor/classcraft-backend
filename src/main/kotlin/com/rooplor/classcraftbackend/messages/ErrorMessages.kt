package com.rooplor.classcraftbackend.messages

object ErrorMessages {
    // firebase auth error messages
    const val FIREBASE_CONFIG_ERROR = "Firebase configuration error"
    const val FIREBASE_CONFIG_NOT_FOUND = "Firebase configuration not found"
    const val FIREBASE_EXCEPTION = "Firebase exception"
    const val FIREBASE_INVALID_ID_TOKEN = "Firebase invalid id token"

    // user error messages
    const val USER_NOT_FOUND = "User not found"
    const val USER_USERNAME_CANNOT_BE_BLANK = "User username cannot be blank"
    const val USER_EMAIL_CANNOT_BE_BLANK = "User email cannot be blank"
    const val USER_EMAIL_INVALID = "User email is invalid"
    const val USER_WITH_EMAIL_ALREADY_EXISTS = "User with email $0 already exists"
    const val USER_WITH_USERNAME_ALREADY_EXISTS = "User with username $0 already exists"
    const val USER_CANNOT_DELETE_OWN_ACCOUNT = "User cannot delete own account"
    const val USER_WITH_ID_DOSE_NOT_EXIST = "User with id $0 does not exist"

    // form error messages
    const val FORM_NOT_FOUND = "Form not found"
    const val MISSING_REQUIRED_FIELDS = "Missing required fields: $0"
    const val ANSWER_NOT_FOUND = "Answer not found"
    const val FIELD_VALIDATE_FAIL = "Field '$0' does not match the validation pattern."
    const val ANSWER_ALREADY_SUBMITTED = "Answer already submitted"

    // change venue status error messages
    const val VENUE_REJECT_REASON_IS_BLANK_OR_NULL = "Reject reason is required when rejecting a venue"
    const val VENUE_STATUS_INVALID = "Venue status is not valid"

    // authorization error messages
    const val UNAUTHORIZED = "Unauthorized"
    const val FORBIDDEN = "Forbidden"
}
