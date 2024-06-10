package com.github.sagiri_kawaii01.blenh.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val DATE_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd")

fun today(): LocalDate {
    return LocalDate.now()
}

fun LocalDate.format(): String {
    return this.format(DATE_PATTERN)
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