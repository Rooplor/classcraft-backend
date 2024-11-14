package com.rooplor.classcraftbackend.helpers

import org.springframework.stereotype.Component

@Component
class FormHelper {
    fun mergeListOfMaps(list: List<Map<String, String>>): Map<String, String> =
        list.fold(mutableMapOf()) { acc, map ->
            acc.putAll(map)
            acc
        }
}
