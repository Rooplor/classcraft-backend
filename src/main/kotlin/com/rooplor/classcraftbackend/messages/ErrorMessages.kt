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
}
