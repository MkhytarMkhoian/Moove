package com.moove.core.kotlin.text

import java.util.regex.Pattern

fun String.matchesPattern(pattern: String): Boolean {
    return Pattern
        .compile("($pattern)")
        .matcher(this)
        .find()
}
