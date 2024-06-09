package com.github.sagiri_kawaii01.blenh.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val DATE_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd")

fun today(): LocalDate {
    return LocalDate.now()
}

fun LocalDate.format(): String {
    return this.format(DATE_PATTERN)
}