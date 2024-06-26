package com.github.sagiri_kawaii01.blenh.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private val DATE_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd")
private val TIME_PATTERN = DateTimeFormatter.ofPattern("HH:mm:ss")

fun today(): LocalDate {
    return LocalDate.now()
}

fun now(): LocalDateTime {
    return LocalDateTime.now()
}

fun LocalDate.format(): String {
    return this.format(DATE_PATTERN)
}

fun String.orNull(): String? {
    return this.ifEmpty {
        null
    }
}

fun LocalTime.format(): String {
    return this.format(TIME_PATTERN)
}

fun formatDate(month: Int, day: Int): String {
    val m = if (month < 10) {
        "0$month"
    } else {
        month.toString()
    }
    val d = if (day < 10) {
        "0$day"
    } else {
        day.toString()
    }
    return "$m-$d"
}

fun <T> Flow<T>.flowOnIo() = this.flowOn(Dispatchers.IO)