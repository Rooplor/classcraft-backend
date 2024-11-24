package com.rooplor.classcraftbackend.enums

enum class FieldValidation(
    val regex: Regex,
) {
    TEXT(Regex(".*")),
    EMAIL(Regex("^[A-Za-z0-9+_.-]+@(.+)$")),
    PHONE(Regex("^\\+?[0-9]{10,13}\$")),
    NUMBER(Regex("^[0-9]+$")),
}
